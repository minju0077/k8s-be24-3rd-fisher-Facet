package com.facet.api.user;

import com.facet.api.common.exception.BaseException;
import com.facet.api.user.model.AuthUserDetails;
import com.facet.api.user.model.EmailVerify;
import com.facet.api.user.model.User;
import com.facet.api.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.facet.api.common.model.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerifyRepository emailVerifyRepository;

    public UserDto.SignupRes signup(UserDto.SignupReq dto) {

        // 이메일 중복 확인
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw BaseException.from(SIGNUP_DUPLICATE_EMAIL);
        }


        User user = dto.toEntity();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        // 메일 전송
        String uuid = UUID.randomUUID().toString();
        emailService.sendWelcomeMail(uuid, dto.getEmail());

        // 메일 전송 내역 저장
        EmailVerify emailVerify = EmailVerify.builder().email(dto.getEmail()).uuid(uuid).build();
        emailVerifyRepository.save(emailVerify);

        return UserDto.SignupRes.from(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> BaseException.from(LOGIN_INVALID_USERINFO)
        );

        return AuthUserDetails.from(user);
    }

    public void verify(String uuid) {
        EmailVerify emailVerify = emailVerifyRepository.findByUuid(uuid).orElseThrow(
                () -> BaseException.from(SIGNUP_INVALID_UUID)
        );
        User user = userRepository.findByEmail(emailVerify.getEmail()).orElseThrow();
        user.setEnable(true);
        userRepository.save(user);
    }

    public UserDto.UserInfoRes getUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> BaseException.from(USER_NOT_FOUND)
        );
        return UserDto.UserInfoRes.from(user);
    }

    public UserDto.UserInfoRes updateUserInfo(String email, UserDto.UserInfoReq req){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> BaseException.from(USER_NOT_FOUND)
        );

        user.setPhoneNumber(req.getPhoneNumber());
        user.setAddress(req.getAddress());
        user.setBirthDate(req.getBirthDate());

        userRepository.save(user);

        return UserDto.UserInfoRes.from(user);
    }

    public void updatePassword(String email, UserDto.PasswordUpdateReq req) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> BaseException.from(USER_NOT_FOUND)
        );
        if(!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw BaseException.from(INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
    }





}
