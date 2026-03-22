package com.facet.api.user;

import com.facet.api.auction.model.AucProdImage;
import com.facet.api.auction.model.AucProduct;
import com.facet.api.auction.model.Bid;
import com.facet.api.auction.repository.AuctionBidRepository;
import com.facet.api.auction.repository.AuctionImageRepository;
import com.facet.api.auction.repository.AuctionReadRepository;
import com.facet.api.common.exception.BaseException;
import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundRewards;
import com.facet.api.funding.order.FundOrdersRepository;
import com.facet.api.funding.order.FundRewardRepository;
import com.facet.api.funding.order.model.FundOrders;
import com.facet.api.funding.order.model.FundOrdersItem;
import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.user.model.EmailVerify;
import com.facet.api.user.model.User;
import com.facet.api.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.facet.api.common.model.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerifyRepository emailVerifyRepository;
    private final AuctionBidRepository auctionBidRepository;
    private final AuctionReadRepository auctionReadRepository;
    private final AuctionImageRepository auctionImageRepository;
    private final FundOrdersRepository fundOrdersRepository;
    private final FundRewardRepository fundRewardRepository;

    public UserDto.SignupRes signup(UserDto.SignupReq dto) {

        // 이메일 중복 확인
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw BaseException.from(SIGNUP_DUPLICATE_EMAIL);
        }


        User user = dto.toEntity();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        // 메일 전송
        String uuid = UUID.randomUUID().toString();
        emailService.sendWelcomeMail(uuid, dto.getEmail());

        // 메일 전송 내역 저장
        EmailVerify emailVerify = EmailVerify.builder().email(dto.getEmail()).uuid(uuid).build();
        emailVerifyRepository.save(emailVerify);

        return UserDto.SignupRes.from(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> BaseException.from(LOGIN_INVALID_USERINFO)
        );

        return AuthUserDetails.from(user);
    }

    public void verify(String uuid) {
        EmailVerify emailVerify = emailVerifyRepository.findByUuid(uuid).orElseThrow(
                () -> BaseException.from(SIGNUP_INVALID_UUID)
        );
        User user = userRepository.findByEmail(emailVerify.getEmail()).orElseThrow();
        user.setEnable(true);
        userRepository.save(user);
    }

    public UserDto.UserInfoRes getUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> BaseException.from(USER_NOT_FOUND)
        );
        return UserDto.UserInfoRes.from(user);
    }

    public UserDto.UserInfoRes updateUserInfo(String email, UserDto.UserInfoReq req){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> BaseException.from(USER_NOT_FOUND)
        );

        user.setPhoneNumber(req.getPhoneNumber());
        user.setAddress(req.getAddress());
        user.setBirthDate(req.getBirthDate());

        userRepository.save(user);

        return UserDto.UserInfoRes.from(user);
    }

    public void updatePassword(String email, UserDto.PasswordUpdateReq req) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> BaseException.from(USER_NOT_FOUND)
        );
        if(!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw BaseException.from(INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
    }

    //  프론트엔드에 던져줄 내 참여 내역(경매+펀딩) 조회 메서드
    public List<UserDto.HistoryDto> getMyHistory(Long userIdx) {
        List<UserDto.HistoryDto> historyList = new ArrayList<>();

        // ---------------------------------------------------
        // 1. [펀딩] 내역 가져오기
        // ---------------------------------------------------
        List<FundOrders> fundOrders = fundOrdersRepository.findAllByOrdersIdx(userIdx);
        for (FundOrders order : fundOrders) {
            if (order.getOrdersItems() != null && !order.getOrdersItems().isEmpty()) {
                FundOrdersItem item = order.getOrdersItems().get(0);

                // 🌟 방어 로직: 객체가 null이면, 우리가 직접 리워드 번호(ProductIdx)로 DB에서 찾아옵니다!
                FundRewards reward = item.getFundRewards();
                if (reward == null && item.getProductIdx() != null) {
                    reward = fundRewardRepository.findById(item.getProductIdx()).orElse(null);
                }

                // 찾아온 리워드와 펀딩 상품이 정상적으로 존재할 때만 리스트에 추가
                if (reward != null && reward.getFundProduct() != null) {
                    FundProduct product = reward.getFundProduct();

                    // DB 상태(영문)를 한글로 변환해서 프론트에 전달 (선택 사항)
                    String statusStr = order.getStatus();
                    if ("PAID".equals(statusStr)) statusStr = "결제완료";
                    else if ("CANCELLED".equals(statusStr)) statusStr = "결제취소";

                    historyList.add(UserDto.HistoryDto.builder()
                            .type("FUNDING")
                            .productIdx(product.getIdx())
                            .name(product.getName())
                            .image(product.getImg())
                            .myPrice(order.getPrice())
                            .currentPrice(null)
                            .date("-")
                            .status(statusStr)
                            .build());
                }
            }
        }

        // ---------------------------------------------------
        // 2. [경매] 내역 가져오기
        // ---------------------------------------------------
        List<Bid> bids = auctionBidRepository.findAllByUserIdx(userIdx);
        for (Bid bid : bids) {
            // 경매 상품 기본 정보 가져오기 (JpaRepository에서 기본 제공되는 findById 사용)
            AucProduct product = auctionReadRepository.findById(bid.getAucProductIdx()).orElse(null);

            if (product != null) {
                // 💡 핵심: 경매 상품의 이미지는 별도 테이블에 있으므로 따로 조회해야 합니다!
                AucProdImage prodImage = auctionImageRepository.findByAucProductIdx(product.getIdx());
                String imageUrl = (prodImage != null) ? prodImage.getImagePath() : "";

                // DB의 status 값(숫자)에 따라 문자열 변환
                String statusStr = (product.getStatus() == 1) ? "진행중" : "종료";

                historyList.add(UserDto.HistoryDto.builder()
                        .type("AUCTION")
                        .productIdx(product.getIdx())
                        .name(product.getName())
                        .image(imageUrl)
                        .myPrice(bid.getBidPrice())
                        .currentPrice(product.getCurrentPrice())
                        .date("-")
                        .status(statusStr)
                        .build());
            }
        }

        return historyList;
    }




}
