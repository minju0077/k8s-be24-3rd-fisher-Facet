package com.facet.api.config;

/*
기존 스프링 시큐리티의 로그인 처리 방식

1. UsernamePasswordAuthenticationFilter 에서 ID(username), PW(password) 를 받아서             (컨트롤러 역할)
2. UsernamePasswordAuthenticationToken 객체에 담아서                                          (Dto 역할)
3. AuthenticationManager 인터페이스를 상속받은 ProviderManager 객체의 authenticate 메소드 실행    (서비스 메소드 실행 역할)

4. 3번에서 실행된 메소드에서 AbstractUserDetailsAuthenticationProvider 객체의 authenticate 메소드 실행
5. 4번에서 실행된 메소드에서 retrieveUser 메소드 실행하고 retrieveUser메소드에서 InMemoryUserDetailsManager 객체의 loadUserByUsername 메소드 실행
6. loadUserByUsername 메소드에서 사용자 정보를 조회해서 해당 하는 사용자가 있으면 UserDetails 객체를 반환
7. 8. 9. 반환받은 걸 확인해서 세션에 사용자 인증 정보 저장
 */

/*

요청 바꾸기 : UsernamePasswordAuthenticationFilter의 attemptAuthentication 메소드를 재정의
사용자 확인 : UserDetailsService의 loadUserByUsername 메소드를 재정의
응답 바꾸기 : UsernamePasswordAuthenticationFilter의 successfulAuthentication 메소드 재정의
*/

import com.facet.api.config.filter.JwtFilter;
import com.facet.api.config.oauth2.OAuth2AuthenticationSuccessHandler;
import com.facet.api.config.oauth2.OAuth2AuthorizationRequestRepository;
import com.facet.api.user.OAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.oauth2Login(config -> {
            config.authorizationEndpoint(endpoint ->
                    endpoint.authorizationRequestRepository(oAuth2AuthorizationRequestRepository)
            );

            config.userInfoEndpoint(
                    endpoint ->
                            endpoint.userService(oAuth2UserService)
            );
            config.successHandler(oAuth2AuthenticationSuccessHandler);
        });

        http.authorizeHttpRequests(
                (auth) -> auth
                        .requestMatchers("/auction/list","/user/login", "/user/signup", "/user/verify").permitAll()
                        .requestMatchers("/board/reg","/auction/detail/*").authenticated()
                        .requestMatchers("/board/reg","/point/**").authenticated()
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
        ).exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
            // 리다이렉트 하지 않고, 401 상태 코드와 에러 메시지를 JSON으로 보냅니다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": \"AUTH_REQUIRED\", \"message\": \"로그인이 필요합니다.\"}");
        }));

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 적용
        return source;
    }

    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlEntryPoint() {
        // 기본적으로 302 리다이렉트를 처리해주는 스프링 시큐리티 클래스입니다.
        return new LoginUrlAuthenticationEntryPoint("http://localhost:5173/user/login");
    }
}
