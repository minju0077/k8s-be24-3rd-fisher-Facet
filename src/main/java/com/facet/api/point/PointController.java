package com.facet.api.point;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.common.model.BaseResponseStatus;
import com.facet.api.point.model.PointDto;
import com.facet.api.user.UserRepository;
import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.facet.api.common.model.BaseResponseStatus.*;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
@Tag(name = "유저 포인트 결제 기능")
public class PointController {
    private final PointService pointService;
    private final UserRepository userRepository;

    //[1단계] 프론트에서 결제 금액 선택하고 충전하기 눌렀을 때
    @PostMapping("/create")
    @Operation(summary = "포인트 충전 결제 주문서 생성",
            description = "DB에 '결제 대기' 상태의 주문서를 생성하고 실제 결제(PG사 연동) 진행에 필요한 주문 번호 등의 데이터를 반환 \n"+
                    "--- \n" +
                    "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다."
    )
    public ResponseEntity<BaseResponse<PointDto.CreateRes>> createPointOrder(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            @RequestBody PointDto.CreateReq dto) {

        // 1. 서비스 호출 : DB에 '결제 대기(false)' 상태로 주문서를 만들고 결과(주문번호 등)를 받아옴
        PointDto.CreateRes result = pointService.createPointOrder(userDetails.toEntity(), dto);

        // 2. 프론트에게 결제대기 상태 코드와 응답 데이터 보내주기
        return ResponseEntity.ok(BaseResponse.ready_point(result));
    }

    //[2단계] 결제 검증 및 충전
    @PostMapping("/verify")
    @Operation(summary = "결제 검증 및 실제 포인트 충전",
            description = "전달받은 결제 내역의 금액과 위변조 여부를 검증하고 이상이 없을 경우 사용자의 실제 보유 포인트를 충전 처리 \n\n"+
                    "--- \n" +
                    "### ⚠️ 주요 에러 코드 및 대응\n" +
                    "* **`2000` 성공** : 결제 확정 (배송지 입력 페이지 이동)\n" +
                    "* **`4102` 주문 미존재** : 존재하지 않는 주문 번호 (알림 후 리스트 이동)\n" +
                    "* **`4106` 유저 미찾음** : 인증 정보 오류 (로그아웃 및 로그인 이동)\n" +
                    "* **`4107` 금액 위변조** : 결제 금액 불일치 (**자동 환불 처리**)\n" +
                    "* **`4100` 시스템 오류** : 통신 실패 등 일반 오류 (다시 시도 안내)\n\n" +

                    "--- \n" +
                    "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다."
    )
    public ResponseEntity<BaseResponse> verifyAndChargePoint(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            @RequestBody PointDto.VerifyReq dto) {


        try{
            pointService.VerifyAndChargePoint(userDetails.toEntity(), dto);

            // 성공 시
            return ResponseEntity.ok(BaseResponse.success(SUCCESS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/current")
    @Operation(summary = "현재 보유 포인트(잔액) 조회",
            description = "로그인된 사용자가 현재 보유하고 있는 포인트 잔액을 조회 , 경매 입찰이나 펀딩 결제를 진행하기 전 잔액 확인 용도로 활용 \n"+
                    "--- \n" +
                    "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다.")
    public ResponseEntity<BaseResponse<PointDto.CurrentRes>> getCurrentPoint(
            @AuthenticationPrincipal AuthUserDetails userDetails){

        User realUser = userRepository.findById(userDetails.toEntity().getIdx())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 유저 엔티티에서 포인트 꺼내서 DTO 담아주기
        PointDto.CurrentRes result = PointDto.CurrentRes.builder()
                .currentPoint(realUser.getPoint())
                .build();

        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
