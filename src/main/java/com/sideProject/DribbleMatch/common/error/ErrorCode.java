package com.sideProject.DribbleMatch.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    /*
    - 첫번째 자리: 도메인
        0: Auth / Token
        1: User
        2: Team
        3: Personal Matching
        4: Team Matching
        5: Personal Match Join
        6: Team Match Join
        7: UserTeam
        8: Recruitment
        9: region
        10: global

    - 두번째 자리: 에러 종류
        0: 도메인 / Dto 객체 생성 오류 (MethodArgumentValidException, ConstraintViolationException 등)
        1: 도메인 로직 상의 오류
        2: 인증, 인가가 안된 오류
        3: 존재하지 않는 리소스에 대한 접근 오류
        4: 외부 API 관련 오류

    */

    TEST_ERROR(400, "9999", "테스트 에러 처리"),

    INVALID_ACCESS_TOKEN(401, "0200", "ACCESS TOKEN 인증 오류"),
    INVALID_REFRESH_TOKEN(401, "0201", "REFRESH TOKEN 인증 오류"),
    NO_AUTHORITY(401, "0201", "REFRESH TOKEN 인증 오류"),

    NOT_UNIQUE_EMAIL(400, "1100", "이메일이 이미 존재합니다."),
    NOT_SAME_PASSWORD(400, "1101", "비밀번호가 다릅니다."),
    INVALID_PASSWORD_PATTERN(400, "1102", "비밀번호는 대문자, 소문자, 숫자, 특수 문자가 하나 이상 포함되어야 합니다."),
    INVALID_PASSWORD(400, "1103", "비밀 번호가 틀렸습니다."),
    NOT_UNIQUE_NICKNAME(400, "1104", "닉네임이 이미 존재합니다."),
    NOT_FOUND_EMAIL(404, "1300", "해당 이메일이 존재하지 않습니다."),
    NOT_FOUND_USER_ID(404, "1301", "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_NICKNAME(404, "1302", "해당 닉네임이 존재하지 않습니다."),

    NOT_UNIQUE_TEAM_NAME(400, "2100", "팀 이름이 이미 존재합니다."),
    NOT_FOUND_TEAM_ID(400, "2300", "해당 팀이 존재하지 않습니다."),
    ALREADY_MEMBER(400, "2000", "이미 등록된 멤버입니다"),
    NOT_FOUND_TEAM_APPLICATION(400, "2301", "해당 가입신청이 존재하지 않습니다."),
    NOT_FOUND_TEAM_MEMBER(404, "2302", "해당 팀원이 존재하지 않습니다."),

    NOT_FOUND_PERSONAL_MATCHING_ID(404, "3300", "해당 개인 경기가 존재하지 않습니다."),

    NOT_FOUND_TEAM_MATCHING_ID(404, "4300", "해당 팀 경기가 존재하지 않습니다."),

    NOT_FOUND_PERSONAL_MATCH_JOIN_ID(404, "5300", "해당 개인 경기 참가 정보가 존재하지 않습니다."),

    NOT_FOUND_TEAM_MATCH_JOIN_ID(404, "6300", "해당 팀 경기 참가 정보가 존재하지 않습니다."),

    NOT_FOUND_USERTEAM_ID(404, "7300", "해당 소속팀 정보가 존재하지 않습니다."),

    NOT_FOUND_RECRUITMENT_ID(404, "8300", "해당 모집글이 존재하지 않습니다."),

    NOT_FOUND_REGION_ID(404, "9300", "해당 지역이 존재하지 않습니다."),
    NOT_FOUND_REGION_STRING(404, "9301", "해당 지역 문자열에 해당하는 지역이 존재하지 않습니다."),

    INVALID_DATA_PATTERN(400, "10000", "입력 값의 형식이 맞지 않습니다."),
    MISSING_REQUEST_HEADER(400, "10001", "요청 헤더에 빈 값이 있습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}
