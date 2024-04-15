package com.sideProject.DribbleMatch.common.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
