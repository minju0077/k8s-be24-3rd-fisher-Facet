package com.facet.api.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {
    // 2000번대 성공
    SUCCESS(true, 2000, "요청이 성공했습니다"),
    READY_POINT(true, 2001, "결제 대기"),
    DONE_POINT(true, 2002,"결제 완료"),
    CANCELED(true,2003,"결제 취소"),


    // 3000번대 클라이언트 입력 오류, 입력값 검증 오류
    JWT_EXPIRED(false, 3001, "JWT 토큰이 만료되었습니다."),
    JWT_INVALID(false, 3002, "JWT 토큰이 유효하지 않습니다."),
    SIGNUP_DUPLICATE_EMAIL(false, 3003, "중복된 이메일입니다."),
    SIGNUP_INVALID_PASSWORD(false, 3004, "비밀번호는 대,소문자, 숫자, 특수문자가 포함되어야 합니다."),
    SIGNUP_INVALID_UUID(false, 3005, "유효하지 않은 인증값입니다. 이메일 인증을 다시 시도해주세요."),
    LOGIN_INVALID_USERINFO(false, 3006, "이메일이나 비밀번호를 확인해주세요."),
    LOGIN_NOT_ENABLED(false, 3007, "이메일 인증을 받고 로그인을 다시 해주세요."),


    // 4000번대
    REQUEST_ERROR(false, 4001, "입력값이 잘못되었습니다."),


    // 4100번대: 결제(Payment) 관련 오류
    PAYMENT_FAIL(false,4100,"결제 실패"),
    PAYMENT_INVALID_AMOUNT(false, 4101, "결제 금액이 일치하지 않습니다."),
    PAYMENT_NOT_FOUND(false, 4102, "존재하지 않는 주문 번호입니다."),
    PAYMENT_NOT_PAID(false, 4103, "결제가 완료되지 않았거나 취소된 결제입니다."),
    PAYMENT_VERIFY_FAIL(false, 4104, "결제 검증에 실패했습니다."),
    PAYMENT_READY_IS_LONG(false,4105,"대기 시간을 초과했습니다."),
    PAYMENT_USER_NOT_FOUND(false,4106,"유저를 찾을 수 없습니다."),
    PAYMENT_AMOUNT_FALSE(false,4107,"결제 금액이 위조되었습니다"),




    // 5000번대 실패
    FAIL(false, 5000, "요청이 실패했습니다"),
    USER_NOT_FOUND(false,5001,"유저를 찾을 수 없습니다.");
    private final boolean success;
    private final int code;
    private final String message;
}
