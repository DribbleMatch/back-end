package com.sideProject.DribbleMatch.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class ApiResponse<T> {
    private final int httpStatus;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public ApiResponse(int httpStatus, String code, String message, T data){
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), null, HttpStatus.OK.name(), data);
    }

    public static ApiResponse<?> error(ErrorCode errorCode) {
        return new ApiResponse<>(
                errorCode.getHttpStatus(),
                errorCode.getCode(),
                errorCode.getMessage(),
                null);
    }

    public static ApiResponse<?> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(
                errorCode.getHttpStatus(),
                errorCode.getCode(),
                message,
                null);
    }
}
