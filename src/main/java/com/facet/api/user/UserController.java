package com.facet.api.user;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.user.model.UserDto;
import com.facet.api.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@Tag(name = "회원 기능")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 기능", description = "Facet 서비스 이용을 위한 새로운 사용자 계정 생성")
    public ResponseEntity signup(@RequestBody UserDto.SignupReq dto) {
        UserDto.SignupRes result = userService.signup(dto);

        return ResponseEntity.ok(result);
    }


    @PostMapping("/login")
    @Operation(summary = "로그인 기능", description = "이메일과 비밀번호를 검증하여 ATOKEN 쿠키를 발급.")
    public ResponseEntity login(@RequestBody UserDto.LoginReq dto) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(), null);

        Authentication authentication = authenticationManager.authenticate(token);
        System.out.println(authentication);
        AuthUserDetails user = (AuthUserDetails) authentication.getPrincipal();

        if (user != null) {
            String jwt = jwtUtil.createToken(user.getIdx(), user.getUsername(), user.getRole(), user.getName());
            UserDto.LoginRes rseult = UserDto.LoginRes.builder()
                    .idx(user.getIdx())
                    .email(user.getUsername())
                    .userName(user.getName())
                    .role(user.getRole())
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", "ATOKEN=" + jwt + "; Path=/")
                    .body(rseult);
        }

        return ResponseEntity.ok("로그인 실패");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 기능", description = "브라우저에 저장된 ATOKEN 쿠키를 즉시 만료시켜 로그아웃 처리")
    public ResponseEntity<String> logout() {
        // 1. 만료 시간이 0인 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("ATOKEN", "")
                .path("/")
                .maxAge(0) // 즉시 만료
                .httpOnly(true) // 자바스크립트 접근 방지 (보안)
                .secure(true) // HTTPS에서만 전송 (권장)
                .sameSite("Strict") // CSRF 방지
                .build();

        // 2. 응답 헤더에 쿠키 설정하여 반환
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("로그아웃 되었습니다.");
    }

    @GetMapping("/verify")
    @Operation(summary = "이메일 인증 처리 기능", description = "회원가입 후 발급된 UUID를 검증하여 계정을 활성화하고 프론트로 리다이렉트")
    public ResponseEntity verify(String uuid) {
        userService.verify(uuid);
        // 인증 성공하면 프론트로 리다이렉트
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create("http://localhost:5173")).build();
    }

    @GetMapping("/callback")
    @Operation(summary = "소셜 로그인 사용자 정보 불러오기 기능 ", description = "소셜 로그인 이후 사용자의 정보를 불러오기 위한 기능 \n"+
            "--- \n" + "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다."
    )
    public ResponseEntity callback(@AuthenticationPrincipal AuthUserDetails user) {

        UserDto.LoginRes rseult = UserDto.LoginRes.builder()
                .idx(user.getIdx())
                .email("kakao")
                .userName(user.getName())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(rseult);
    }

    // 토큰이 유효한지 확인하는 메소드
    @GetMapping("/validate")
    @Operation(summary = "토큰이 유효한지 확인하는 기능", description = " 토큰이 만료되지는 않았는지, 위조되지 않았는지 최종 확인")
    public ResponseEntity validate(
            @RequestHeader("ATOKEN") String token
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("헤더가 없거나 형식이 잘못됨");
        }

        String isAuth = token.substring(7);

        // 드디어 우리가 만든 검증기 작동!
        if (jwtUtil.validateToken(isAuth)) {
            return ResponseEntity.ok("유효한 토큰입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }

    @GetMapping("/getuserinfo")
    @Operation(summary = "내 정보 조회 기능", description = "현재 로그인된 사용자의 이름, 연락처, 주소, 생일 등 전체 프로필 정보를 조회, 연락처와 주소, 생일 정보는 사용자가 이전에 [내 정보 수정]을 통해 저장한 경우에만 표시. \n"
    +"--- \n" + "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다.")
    public ResponseEntity getUserInfo(
            @AuthenticationPrincipal AuthUserDetails user){
        UserDto.UserInfoRes result = userService.getUserInfo(user.getUsername());
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @PostMapping("/updateuserinfo")
    @Operation(summary = "내 정보 수정 기능", description = "현재 로그인된 사용자의 핸드폰 번호, 주소, 생일 정보를 업데이트 \n"
            +"--- \n" + "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다.")
    public ResponseEntity updateUserInfo(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody UserDto.UserInfoReq dto
    ) {
        UserDto.UserInfoRes result = userService.updateUserInfo(user.getUsername(), dto);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @PostMapping("/updatepassword")
    @Operation(summary = "비밀번호 변경 기능", description = "현재 사용 중인 비밀번호를 확인한 후 새로운 비밀번호로 변경 \n"
            +"--- \n" + "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다.")
    public ResponseEntity updatepassword(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody UserDto.PasswordUpdateReq dto
    ){
        userService.updatePassword(user.getUsername(), dto);
        return ResponseEntity.ok(BaseResponse.success("비밀번호가 변경되었습니다."));
    }

    @GetMapping("/history")
    @Operation(summary = "마이페이지 활동 내역(주문 / 참여 내역)", description = "사용자가 참여한 펀딩 및 경매 내역과 상태별 요약 통계를 조회 \n"
            +"--- \n" + "**※ 주의:** JWT 인증 쿠키가 반드시 포함되어야 합니다.")
    public ResponseEntity getMyHistory(@AuthenticationPrincipal AuthUserDetails userDetails) {

        // 1. 방금 만든 UserService의 메서드를 호출해서 데이터 가져오기
        List<UserDto.HistoryDto> historyList = userService.getMyHistory(userDetails.getIdx());

        // 2. 프론트엔드 상단에 띄울 요약 데이터(Summary) 계산하기
        long ongoingCount = historyList.stream()
                .filter(h -> "진행중".equals(h.getStatus()) || "PAID".equals(h.getStatus()) || "PENDING".equals(h.getStatus()))
                .count();
        long endedCount = historyList.size() - ongoingCount;

        Map<String, Object> summary = new HashMap<>();
        summary.put("total", historyList.size());
        summary.put("ongoing", ongoingCount);
        summary.put("ended", endedCount);

        // 3. 리스트와 요약 데이터를 하나의 맵(Map)으로 포장하기
        Map<String, Object> result = new HashMap<>();
        result.put("historyList", historyList);
        result.put("summary", summary);

        // 4. 프론트엔드로 발사!
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
