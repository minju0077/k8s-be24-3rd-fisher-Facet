package com.facet.api.funding.order;

import com.facet.api.funding.FundingRepository;
import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundRewards;
import com.facet.api.funding.order.model.FundOrders;
import com.facet.api.funding.order.model.FundOrdersDto;
import com.facet.api.funding.order.model.FundOrdersItem;
import com.facet.api.user.model.AuthUserDetails;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jose.shaded.gson.ToNumberPolicy;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.portone.sdk.server.payment.PaymentClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundOrdersService {
    private final FundOrdersRepository fundOrdersRepository;
    private final FundItemRepository fundItemRepository;
    private final FundingRepository fundingRepository;
    private final FundRewardRepository fundRewardRepository;

    private final PaymentClient pg;

    public FundOrdersDto.OrdersRes create(AuthUserDetails user, FundOrdersDto.OrdersReq dto) {
        // 주문한 리워드 불러오기
        List<FundRewards> productList = fundRewardRepository.findAllById(dto.getFundIdxList());

        FundOrders fundOrders = fundOrdersRepository.save(dto.toEntity(user.toEntity()));

        // 주문한 상품을 하나씩 뽑아서 DB에 담는다
        for(FundRewards Reward : productList){
            FundOrdersItem fundOrdersItem = FundOrdersItem.builder()
                    .fundRewards(Reward)
                    .fundOrders(fundOrders)
                    .build();
            fundItemRepository.save(fundOrdersItem);
        }

        // 주문자 idx, 결제 여부
        return FundOrdersDto.OrdersRes.from(fundOrders);
    }


    // 결제 검증 및 완료 처리
    public void verify(AuthUserDetails user, FundOrdersDto.VerifyReq dto) {

        // pg사에 페이먼트id 를 요청한다. completableFuture를 통해서 비동기로 요청
        CompletableFuture<Payment> completableFuture = pg.getPayment(dto.getPaymentId());
        // join()은 비동기 작업이 끝날때까지 기다렸다가 받아오겠다
        io.portone.sdk.server.payment.Payment payment = completableFuture.join();

        System.out.println(payment);
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
            FundOrders orders = fundOrdersRepository.findById(ordersIdx).orElseThrow();

            // 상품을 하나씩 꺼내서 금액을 결제
            int totalPrice = orders.getItems().stream()
                    .map(FundOrdersItem::getFundRewards)
                    .mapToInt(FundRewards::getPrice)
                    .sum();

            // 금액을 검증하고 확정한다.
            if(paidPayment.getAmount().getTotal() == totalPrice+3000) {
                orders.setPaid(true);
                orders.setPgPaymentId(dto.getPaymentId());
                fundOrdersRepository.save(orders);
            }

        }
    }
}
