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
            description = "사용자가 펀딩 리워드를 선택하고 결제를 시도할 때 호출, DB에 주문 내역을 임시 저장하고 포트원 호출 시 customData에 저장할 데이터를 반환 \n"+
                    "--- \n" +
                    "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다."
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
            summary = "결제 검증 및 펀딩 확정 기능",
            description = "포트원(PortOne) 결제 완료 후 호출하면 PG사에 실제 결제 금액을 조회합니다. 이후 주문 DB의 금액과 비교하여 위변조 여부를 검증합니다. \n" +
                    " - 검증 성공 시: 상품 재고 차감, 서포터즈 수 및 현재 금액, 펀딩 달성률 업데이트, 주문 상태를 'PAID'로 변경합니다. \n" +
                    " - 검증 실패 시: 결제를 즉시 자동 취소하고 주문을 'CANCELLED'로 변경합니다. \n\n" +
                    "--- \n" +
                    "### ⚠️ 결제 검증 결과 코드별 대응 가이드\n" +
                    "* **`2002` 결제 완료** : 정상 처리 (배송지 입력 페이지로 이동)\n" +
                    "* **`4100` 결제 실패** : PG사 결제 실패 (다시 시도 안내)\n" +
                    "* **`4101` 금액 불일치** : 위변조 의심 (**자동 결제 취소** 후 리스트 이동)\n" +
                    "* **`4106` 유저 미찾음** : 인증 정보 오류 (로그인 페이지 이동)\n" +
                    "* **`4108` 취소 오류** : 자동 취소 실패 (관리자 확인 필요/리스트 이동)\n" +
                    "* **`4109` 권한 오류** : 타인 주문 결제 시도 (로그아웃 및 로그인 이동)\n" +
                    "* **`4203` 리워드 미찾음** : 상품 정보 부재 (펀딩 리스트 이동)\n\n" +
                    "--- \n" +
                    "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다."
    )
    public ResponseEntity verify(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody FundOrdersDto.VerifyReq dto) {
        BaseResponseStatus result = fundOrdersService.verify(user,dto);
        return ResponseEntity.ok(BaseResponse.fundOrders(result,result));
    }


}
