package com.facet.api.user;

import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.user.model.User;
import com.facet.api.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2UserService
        extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("서비스 코드 실행");

        String provider = userRequest
                .getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = super.loadUser(userRequest);

        UserDto.OAuth dto = UserDto.OAuth.from(oAuth2User.getAttributes(), provider);

        Optional<User> result = userRepository.findByEmail(dto.getEmail());

        if(!result.isPresent()) {
            User user = userRepository.save(dto.toEntity());

            return AuthUserDetails.from(user);
        }
        else {
            User user = result.get();

            return AuthUserDetails.from(user);
        }
    }
}