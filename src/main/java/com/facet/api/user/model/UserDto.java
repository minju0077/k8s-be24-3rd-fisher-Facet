package com.facet.api.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

public class UserDto {

    @Getter
    @Builder
    // 소셜 로그인
    public static class OAuth {
        private String email;
        private String name;
        private String provider;
        private boolean enable;
        private String role;

        public static OAuth from(Map<String, Object> attributes, String provider) {
            String email = null;
            String name = null;

            if(provider.equals("kakao")) {
                String providerId = ((Long) attributes.get("id")).toString();
                email = providerId + "@kakao.social";
                Map properties = (Map) attributes.get("properties");
                name = (String) properties.get("nickname");
            } else if(provider.equals("google")){
                email = (String)attributes.get("email");
                name = (String)attributes.get("name");

            }

            return OAuth.builder()
                    .email(email)
                    .name(name)
                    .provider(provider)
                    .enable(true)
                    .role("ROLE_USER")
                    .build();
        }

        public User toEntity() {
            return User.builder()
                    .email(this.email)
                    .name(this.name)
                    .password(this.provider)
                    .enable(this.enable)
                    .role(this.role)
                    .point(0)
                    .build();
        }
    }

    @Getter
    public static class SignupReq {
        private String email;
        private String name;
        private String password;

        public User toEntity() {
            return User.builder()
                    .email(this.email)
                    .name(this.name)
                    .password(this.password)
                    .enable(false)
                    .role("ROLE_USER")
                    .build();
        }
    }


    @Builder
    @Getter
    public static class SignupRes {
        private Long idx;
        private String email;
        private String name;

        public static SignupRes from(User entity) {
            return SignupRes.builder()
                    .idx(entity.getIdx())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .build();
        }
    }

    @Getter
    public static class LoginReq {
        private String email;
        private String password;
    }

    @Builder
    @Getter
    public static class LoginRes {
        private Long idx;
        private String email;
        private String userName;
        private String role;

        public static LoginRes from(User entity) {
            return LoginRes.builder()
                    .idx(entity.getIdx())
                    .email(entity.getEmail())
                    .userName(entity.getName())
                    .build();
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class UserInfoReq {
        private String phoneNumber;
        private String address;
        private LocalDate birthDate;
    }

    @Builder
    @Getter
    public static class UserInfoRes {
        private String name;
        private String email;
        private Integer point;
        private String phoneNumber;
        private String address;
        private LocalDate birthDate;

        public static UserInfoRes from(User entity) {
            return UserInfoRes.builder()
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .point(entity.getPoint())
                    .phoneNumber(entity.getPhoneNumber())
                    .address(entity.getAddress())
                    .birthDate(entity.getBirthDate())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PasswordUpdateReq {
        private String currentPassword; // 현재 비밀번호 (확인용)
        private String newPassword;     // 새 비밀번호
    }

    @Getter
    @Builder
    public static class HistoryDto {
        private String type;          // "AUCTION" 또는 "FUNDING"
        private Long productIdx;      // 상품 번호 (상세 페이지 이동용)
        private String name;          // 상품 이름
        private String image;         // 상품 썸네일 이미지 URL
        private Long myPrice;         // 내 입찰가 / 내 펀딩 금액
        private Long currentPrice;    // 경매 현재 최고가 (펀딩은 null)
        private String date;          // 날짜 (생략하기로 했으니 "-" 등으로 처리) 추가해 줘야함
        private String status;        // 진행 상태
    }
}
