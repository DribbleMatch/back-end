package com.sideProject.DribbleMatch.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400
    TEST_ERROR(400, "테스트 에러 처리"),

    // 404
    INVALID_USER_ID(404, "해당 사용자가 존재하지 않습니다."),
    INVALID_TEAM_ID(404, "해당 팀이 존재하지 않습니다."),
    INVALID_USERTEAM_ID(404, "해당 소속팀 정보가 존재하지 않습니다."),
    INVALID_MATCHING_ID(404, "해당 경기가 존재하지 않습니다."),
    INVALID_TEAM_MATCH_JOIN_ID(404, "해당 팀경기 참가 정보가 존재하지 않습니다."),
    INVALID_PERSONAL_MATCH_JOIN_ID(404, "해당 개인 경기 참가 정보가 존재하지 않습니다."),
    INVALID_RECRUITMENT_ID(404, "해당 모집글이 존재하지 않습니다.");

    private final int status;
    private final String message;
}
