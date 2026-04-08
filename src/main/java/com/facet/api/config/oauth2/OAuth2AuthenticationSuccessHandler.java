package com.facet.api.config.oauth2;

import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OAuth 2.0 로그인 성공");

        AuthUserDetails user = (AuthUserDetails)authentication.getPrincipal();

        String jwt = jwtUtil.createToken(user.getIdx(), user.getUsername(), user.getRole(), user.getName());
        response.addHeader("Set-Cookie", "ATOKEN=" + jwt + "; Path=/");
        String redirectUrl = "https://www.facet7.kro.kr/kakaoCallBack";



        // 4. 저장해둔 REDIRECT_URI 쿠키가 있는지 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("REDIRECT_URI")) {
                    String savedPath = cookie.getValue();
                    // URL에 쿼리 스트링으로 목적지 추가
                    redirectUrl += "?redirect=" + savedPath;

                    // 사용한 쿠키 삭제
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
}