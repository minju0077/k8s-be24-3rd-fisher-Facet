package com.facet.api.user;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.user.model.UserDto;
import com.facet.api.utils.JwtUtil;
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
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDto.SignupReq dto) {
        UserDto.SignupRes result = userService.signup(dto);

        return ResponseEntity.ok(result);
    }


    @PostMapping("/login")
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
    public ResponseEntity verify(String uuid) {
        userService.verify(uuid);
        // 인증 성공하면 프론트로 리다이렉트
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create("http://localhost:5173")).build();
    }

    @GetMapping("/callback")
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
    public ResponseEntity getUserInfo(
            @AuthenticationPrincipal AuthUserDetails user){
        UserDto.UserInfoRes result = userService.getUserInfo(user.getUsername());
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @PostMapping("/updateuserinfo")
    public ResponseEntity updateUserInfo(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody UserDto.UserInfoReq dto
    ) {
        UserDto.UserInfoRes result = userService.updateUserInfo(user.getUsername(), dto);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @PostMapping("/updatepassword")
    public ResponseEntity updatepassword(
            @AuthenticationPrincipal AuthUserDetails user,
            @RequestBody UserDto.PasswordUpdateReq dto
    ){
        userService.updatePassword(user.getUsername(), dto);
        return ResponseEntity.ok(BaseResponse.success("비밀번호가 변경되었습니다."));
    }

    @GetMapping("/history")
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
