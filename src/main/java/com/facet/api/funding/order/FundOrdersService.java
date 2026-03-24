package com.facet.api.funding.order;

import com.facet.api.common.exception.BaseException;
import com.facet.api.common.model.BaseResponseStatus;
import com.facet.api.funding.FundingRepository;
import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundRewards;
import com.facet.api.funding.order.model.FundOrders;
import com.facet.api.funding.order.model.FundOrdersDto;
import com.facet.api.funding.order.model.FundOrdersItem;
import com.facet.api.user.model.AuthUserDetails;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jose.shaded.gson.ToNumberPolicy;
import io.portone.sdk.server.payment.CancelPaymentResponse;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.portone.sdk.server.payment.PaymentClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.facet.api.common.model.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundOrdersService {
    private final FundOrdersRepository fundOrdersRepository;
    private final FundRewardRepository fundRewardRepository;
    private final FundingRepository fundingRepository;

    private final PaymentClient pg;

    // 결제 검증 및 완료 처리
    @Transactional
    public BaseResponseStatus verify(AuthUserDetails user, FundOrdersDto.VerifyReq dto) {

        // pg사에 페이먼트id 를 요청한다. completableFuture를 통해서 비동기로 요청
        CompletableFuture<Payment> completableFuture = pg.getPayment(dto.getPaymentId());
        // join()은 비동기 작업이 끝날때까지 기다렸다가 받아오겠다
        io.portone.sdk.server.payment.Payment payment = completableFuture.join();

        // 가져온 payment 객체가 실제로 결제가 완료된 상태(PaidPayment) 인지 확인
        // 포트원에서 결제 이후 보내준 데이터를 customData에 넣는다.
        if(payment instanceof PaidPayment paidPayment) {
            // json을 map으로 파싱하는 과정
            Map<String, Object> customData = new GsonBuilder()
                    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                    .create().fromJson(paidPayment.getCustomData(), Map.class);

            // customData 안에서 ordersIdx를 꺼내서 string을 Long으로 한다.
            Long ordersIdx = Long.parseLong(customData.get("ordersIdx").toString());

            // id를 조회해서 정보를 가지고 온다.
            FundOrders orders = fundOrdersRepository.findById(ordersIdx)
                    .orElseThrow(() -> new BaseException(PAYMENT_USER_NOT_FOUND));

            if (!orders.getOrdersIdx().equals(user.getIdx())) {
                return BaseResponseStatus.PAYMENT_USER_MISMATCH;
            }

            List<Long> rewards = orders.getOrdersItems().stream()
                    .map(FundOrdersItem::getProductIdx)
                    .toList();

            List<FundRewards> rewardsList = fundRewardRepository.findAllByIdWithPessimisticLock(rewards);


            // 주문 상품에 하나씩 꺼내서 금액을 결제, 상품 금액 * 주문 수량
            long totalPrice = 0;
            for (FundOrdersItem item : orders.getOrdersItems()) {
                // 현재 아이템의 ID와 일치하는 리워드 엔티티를 찾습니다.
                FundRewards matchingReward = rewardsList.stream()
                        .filter(r -> r.getIdx().equals(item.getProductIdx()))
                        .findFirst()
                        .orElseThrow(() -> new BaseException(NOT_FOUND_REWARD));

                // 아이템에 실제 리워드 객체를 연결 (이걸 해야 getFundRewards()가 작동함)
                item.setFundRewards(matchingReward);

                // 금액 합산
                totalPrice += (long) matchingReward.getPrice() * item.getQuantity();
            }

            // 검증 완료 후 결제 정보 저장
            if(paidPayment.getAmount().getTotal() == totalPrice+3000) {
                orders.setStatus("PAID");
                orders.setPgPaymentId(dto.getPaymentId());
                fundOrdersRepository.save(orders);

                // [추가] 1. 주문에 연결된 상품(Product) 엔티티 가져오기
                FundProduct product = orders.getFundProduct();

                // [추가] 2. 상품의 집계 데이터 업데이트
                // 총 모금액 업데이트 (기존 금액 + 현재 주문 금액)
                long updatedAmount = (product.getTargetPrice() != null ? product.getTargetPrice() : 0L) + orders.getPrice();
                product.setTargetPrice(updatedAmount);

                // 총 서포터 수 업데이트 (+1명)
                long updatedSupporters = (product.getSupporters() != null ? product.getSupporters() : 0L) + 1;
                product.setSupporters(updatedSupporters);

                // 달성률(퍼센트) 업데이트: (현재 금액 * 100) / 목표 금액
                if (product.getGoalPrice() != null && product.getGoalPrice() > 0) {
                    long currentPercent = (updatedAmount * 100) / product.getGoalPrice();
                    product.setPercent(currentPercent); // 정렬을 위해 achievementRate 필드에 저장
                }

                // [추가] 3. 변경된 상품 정보 저장 (Dirty Checking이 작동하지만 명시적으로 호출 가능)
                fundingRepository.save(product);

                // 각 리워드 아이템의 재고를 즉시 차감
                for (FundOrdersItem item : orders.getOrdersItems()) {
                    // item이 참조하고 있는 실제 리워드(상품) 엔티티를 가져옵니다.
                    // (엔티티 구조에 따라 getFundRewards() 또는 getProduct() 등으로 호출)
                    FundRewards reward = item.getFundRewards();

                    // 재고 차감 로직 실행 (엔티티 내부에 구현된 메소드 호출)
                    reward.reduceQuantity(item.getQuantity());

                    // 변경된 재고를 DB에 반영 (Dirty Checking 덕분에 생략 가능할 수도 있지만 명시적으로 작성)
                    fundRewardRepository.save(reward);
                }
                return BaseResponseStatus.DONE_POINT;
            }else {

                // 1. 주문 상태를 결제 실패/취소로 변경
                orders.setStatus("CANCELLED");
                fundOrdersRepository.save(orders);

                try {
                    // 2. PG사(포트원)에 비동기 결제 취소 요청
                    CompletableFuture<CancelPaymentResponse> future = pg.cancelPayment(
                            dto.getPaymentId(),
                            null, null, null,
                            "amount-mismatch-error", // 취소 사유
                            null, null, null, null, null, null
                    );

                    // 취소가 완료될 때까지 대기
                    future.join();

                } catch (Exception e) {
                    // 취소 실패 시 로그 기록 (매우 중요: 실제 돈이 나갔을 수 있음)
                    System.err.println("PG사 결제 취소 실패: " + e.getMessage());
                    return BaseResponseStatus.PAYMENT_CANCEL_FAIL;
                }
                return BaseResponseStatus.PAYMENT_INVALID_AMOUNT;
            }
        }
        return BaseResponseStatus.PAYMENT_FAIL;
    }
}

