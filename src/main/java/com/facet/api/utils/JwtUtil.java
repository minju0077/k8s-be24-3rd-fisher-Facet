package com.facet.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String key;

    @Value("${jwt.expire}")
    private int expire;

    public SecretKey getEncodedKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String createToken(Long idx, String email, String role, String name) {
        String jwt = Jwts.builder()
                .claim("idx", idx)
                .claim("email", email)
                .claim("role", role)
                .claim("name",name)
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expire)).signWith(getEncodedKey()).compact();

        return jwt;
    }

    // 로그아웃을 위한 쿠키 생성 메소드, 유효시간을 0으로 세팅해서 토큰 발급
    public String deleteToken() {
        String jwt = Jwts.builder()
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis())).signWith(getEncodedKey()).compact();

        return jwt;
    }

    public Long getUserIdx(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getEncodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("idx", Long.class);
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getEncodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("email", String.class);
    }

    public String getRole(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getEncodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }

    public String getName(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getEncodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("name", String.class);
    }

    public boolean validateToken(String token) {
        try {
            // 토큰을 파싱해봅니다. 문제가 있으면 여기서 예외가 발생해요.
            Jwts.parser()
                    .verifyWith(getEncodedKey())
                    .build()
                    .parseSignedClaims(token);
            return true; // 에러 없이 파싱되면 유효한 토큰!
        } catch (Exception e) {
            // 서명이 다르거나, 만료되었거나, 형식이 잘못되면 false 반환
            System.out.println("토큰 검증 실패: " + e.getMessage());
            return false;
        }
    }
}
