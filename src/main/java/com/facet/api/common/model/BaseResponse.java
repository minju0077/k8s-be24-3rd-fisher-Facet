package com.facet.api.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.facet.api.common.model.BaseResponseStatus.*;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse<T> {
    private Boolean success;
    private Integer code;
    private String message;
    private T result;

    public static <T> BaseResponse success(T result) {
        return new BaseResponse(
                SUCCESS.isSuccess(),
                SUCCESS.getCode(),
                SUCCESS.getMessage(),
                result
        );
    }

    public static <T> BaseResponse ready_point(T result) {
        return new BaseResponse(
                READY_POINT.isSuccess(),
                READY_POINT.getCode(),
                READY_POINT.getMessage(),
                result
        );
    }

    public static <T> BaseResponse done_point(T result) {
        return new BaseResponse(
                DONE_POINT.isSuccess(),
                DONE_POINT.getCode(),
                DONE_POINT.getMessage(),
                result
        );
    }

    public static <T> BaseResponse fail(BaseResponseStatus status) {
        return new BaseResponse(
                status.isSuccess(),
                status.getCode(),
                status.getMessage(),
                null
        );
    }

    public static <T> BaseResponse fail(BaseResponseStatus status, T result) {
        return new BaseResponse(
                status.isSuccess(),
                status.getCode(),
                status.getMessage(),
                result
        );
    }
}
