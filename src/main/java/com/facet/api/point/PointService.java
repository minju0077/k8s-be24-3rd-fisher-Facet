package com.facet.api.point;

import com.facet.api.common.exception.BaseException;
import com.facet.api.common.model.BaseResponseStatus;
import com.facet.api.point.model.Point;
import com.facet.api.point.model.PointDto;
import com.facet.api.user.UserRepository;
import com.facet.api.user.model.User;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.Payment;
import io.portone.sdk.server.payment.PaymentClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static com.facet.api.common.model.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;    // 포인트 충전을 위해 유저 DB 접근 권한
    private final PaymentClient pg;                 //  포트원 서버랑 통신하기 위해

    //  [1단계 결제 사전 등록 (프론트에서 결제창 띄우기 직전에 호출)]
    @Transactional  // 이 메서ㅡ 안에서 일어나는 DB 작업을 하나로 묶끼
    public PointDto.CreateRes createPointOrder(User user, PointDto.CreateReq dto) {

        //  1. DTO가 스스로 조립한 Point 엔티티(결제 대기 상태, 금액 포함)를 가져와서 DB에 저장
        Point point = pointRepository.save(dto.toEntity(user));
        //  2. DB에 방금 저장되면서 발급된 PK 번호(pointIdx)를 프론에 돌려줌
        return PointDto.CreateRes.from(point);

    }
    // [2단계] 포트원 결제 검증 및 실제 포인트 충전 (결제창 완료 후 호출됨)
    @Transactional
    public void VerifyAndChargePoint(User user, PointDto.VerifyReq dto) {

        // 1. 프론트가 가져온 영수증 번호(paymentId)로 포트원 본사 서버에 진짜 결제된 게 맞는지 비동기로 물어봄
        CompletableFuture<Payment> future = pg.getPayment(dto.getPaymentId());
        io.portone.sdk.server.payment.Payment portonePayment = future.join();   //  응답이 올 때까지 기다림

        // 2. 만약 포트원 서버에서 결제 완료가 증명되면 if 문 실행
        if (portonePayment instanceof PaidPayment paidPayment) {

            // 3.
            Point point = pointRepository.findById(dto.getPointIdx())
                    .orElseThrow(() -> BaseException.from(PAYMENT_NOT_FOUND));

            // 4. 포트원에 실제로 결제된 금액 == 처음에 유저가 충전하겠다고 DB에 적어둔 금액(Amount)
            if (paidPayment.getAmount().getTotal() == point.getAmount()) {

                // 5. DB 결제 상태 업데이트
                point.setPaid(true);    //  결제 완료 상태로 변경
                point.setPgPaymentId(dto.getPaymentId());   //  포트원 영수증 기록
                point.setPaidAt(LocalDateTime.now());       //  결제 승인 시간 기록

                // 6. 유저 포인트 충전
                User pointUser = userRepository.findById(user.getIdx())
                        .orElseThrow(() -> BaseException.from(PAYMENT_USER_NOT_FOUND));

                //  기존의 포인트에 결제한 포인트 더하기
                pointUser.addPoint(point.getAmount());

            } else {
                //프론트에서 금액을 조작해서 결제한 경우 여기서 적발
                throw new BaseException(PAYMENT_AMOUNT_FALSE);
            }
        } else {
            // 유저가 결제창을 닫았거나, 카드 한도 초과, 계좌 금액이 충분하지 않아 결제가 실패한 경우
            throw new BaseException(PAYMENT_FAIL);
        }
    }
}
