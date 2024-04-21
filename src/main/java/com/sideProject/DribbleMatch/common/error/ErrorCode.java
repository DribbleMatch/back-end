package com.sideProject.DribbleMatch.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400
    TEST_ERROR(400, "테스트 에러 처리"),
    NOT_SAME_PASSWORD(400, "비밀번호가 다릅니다."),
    INVALID_PASSWORD_PATTERN(400, "비밀번호는 대문자, 소문자, 숫자, 특수 문자가 하나 이상 포함되어야 합니다."),
    INVALID_PASSWORD(400, "비밀 번호가 틀렸습니다."),
    INVALID_DATA_PATTERN(400, "입력 값의 형식이 맞지 않습니다."),
        // unique
    NOT_UNIQUE_EMAIL(400, "이메일이 이미 존재합니다."),
    NOT_UNIQUE_NICKNAME(400, "닉네임이 이미 존재합니다."),

    // 401
    INVALID_ACCESS_TOKEN(401, "ACCESS TOKEN 인증 오류"),
    INVALID_REFRESH_TOKEN(401, "REFRESH TOKEN 인증 오류"),

    // 404
    NOT_FOUND_USER_ID(404, "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_TEAM_ID(404, "해당 팀이 존재하지 않습니다."),
    NOT_FOUND_USERTEAM_ID(404, "해당 소속팀 정보가 존재하지 않습니다."),
    NOT_FOUND_MATCHING_ID(404, "해당 경기가 존재하지 않습니다."),
    NOT_FOUND_TEAM_MATCH_JOIN_ID(404, "해당 팀경기 참가 정보가 존재하지 않습니다."),
    NOT_FOUND_PERSONAL_MATCH_JOIN_ID(404, "해당 개인 경기 참가 정보가 존재하지 않습니다."),
    NOT_FOUND_RECRUITMENT_ID(404, "해당 모집글이 존재하지 않습니다."),
    NOT_FOUND_EMAIL(404, "해당 이메일이 존재하지 않습니다."),
    NOT_FOUND_NICKNAME(404, "해당 닉네임이 존재하지 않습니다."),;

    private final int status;
    private final String message;
}
