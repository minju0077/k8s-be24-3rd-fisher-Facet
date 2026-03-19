package com.facet.api.funding.order;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.common.model.BaseResponseStatus;
import com.facet.api.funding.order.model.FundOrdersDto;
import com.facet.api.user.model.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.facet.api.common.model.BaseResponseStatus.JWT_EXPIRED;

@RequestMapping("/fundOrders")
@RequiredArgsConstructor
@RestController
public class FundOrderController {
    private final FundOrdersService fundOrdersService;



    // 토큰, DTO(총가격, 주문 리워드 IDX) 받아오기
    @PostMapping("/create")
    public ResponseEntity create(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody FundOrdersDto.OrdersReq dto
            ){
        FundOrdersDto.OrdersRes result = fundOrdersService.create(user, dto);
        // 주문자 idx, 결제 여부
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @PostMapping("/verify")
    public ResponseEntity verify(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody FundOrdersDto.VerifyReq dto
    ) {
        fundOrdersService.verify(user, dto);

        return ResponseEntity.ok(BaseResponse.success("성공"));
    }


}
