package com.facet.api.funding.order;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.common.model.BaseResponseStatus;
import com.facet.api.funding.FundingRepository;
import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.order.model.FundOrders;
import com.facet.api.funding.order.model.FundOrdersDto;
import com.facet.api.user.model.AuthUserDetails;
import io.portone.sdk.server.payment.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "*")
@RequestMapping("/fundOrders")
@RequiredArgsConstructor
@RestController
public class FundOrderController {
    private final FundingRepository fundingRepository;
    private final FundOrdersService fundOrdersService;
    private final FundOrdersRepository fundOrdersRepository;
    private final PaymentClient pg;



    // 토큰, DTO(총가격, 주문 리워드 IDX) 받아오기
    @PostMapping("/create")
    public ResponseEntity create(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody FundOrdersDto.OrdersReq dto
            ){

        FundProduct product = fundingRepository.findById(dto.getProductIdx())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        FundOrders orders = fundOrdersRepository.save(dto.toEntity(user.toEntity(), product));
        // 주문자 idx, 결제 여부
        return ResponseEntity.ok(FundOrdersDto.OrdersRes.from(orders));
    }

    @PostMapping("/verify")
    public ResponseEntity verify(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody FundOrdersDto.VerifyReq dto) {
        BaseResponseStatus result = fundOrdersService.verify(user,dto);
        return ResponseEntity.ok(BaseResponse.fundOrders(result,result));
    }


}
