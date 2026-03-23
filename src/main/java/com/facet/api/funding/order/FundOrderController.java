package com.facet.api.funding.order;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.common.model.BaseResponseStatus;
import com.facet.api.funding.FundingRepository;
import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.order.model.FundOrders;
import com.facet.api.funding.order.model.FundOrdersDto;
import com.facet.api.user.model.AuthUserDetails;
import io.portone.sdk.server.payment.PaymentClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "*")
@RequestMapping("/fundOrders")
@RequiredArgsConstructor
@RestController
@Tag(name = "펀딩 결제(주문) 기능")
public class FundOrderController {
    private final FundingRepository fundingRepository;
    private final FundOrdersService fundOrdersService;
    private final FundOrdersRepository fundOrdersRepository;
    private final PaymentClient pg;



    // 토큰, DTO(총가격, 주문 리워드 IDX) 받아오기
    @PostMapping("/create")
    @Operation(
            summary = "펀딩 결제 주문서 생성 기능",
            description = "사용자가 펀딩 리워드를 선택하고 결제를 시도할 때 호출, DB에 주문 내역을 임시 저장하고 결제창(PG사) 호출을 위한 기초 데이터를 반환 (인증 쿠키 필요)"
    )
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
    @Operation(
            summary = "결제 검증 및 실제 포인트 충전 기능",
            description = "포트원(PortOne) 결제 완료 후 호출됩니다. 실제 결제된 금액과 DB의 주문 금액을 비교하여 위변조를 검증하고, 이상이 없을 시 펀딩 참여를 최종 확정합니다. **(인증 쿠키 필요)**"
    )
    public ResponseEntity verify(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody FundOrdersDto.VerifyReq dto) {
        BaseResponseStatus result = fundOrdersService.verify(user,dto);
        return ResponseEntity.ok(BaseResponse.fundOrders(result,result));
    }


}
