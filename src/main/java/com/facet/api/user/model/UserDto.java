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
}
