package com.sideProject.DribbleMatch.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400
    TEST_ERROR(400, "테스트 에러 처리");

    private final int status;
    private final String message;
}
