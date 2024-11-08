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
        3: UserTeam
        4: Matching
        5: Personal Match Join
        6: Team Match Join
        7: Recruitment
        8: TeamApplication
        9: region
        10: stadium
        11: global
        12: etc

    - 두번째 자리: 에러 종류
        0: 도메인 / Dto 객체 생성 오류 (MethodArgumentValidException, ConstraintViolationException 등)
        1: 도메인 로직 상의 오류
        2: 인증, 인가가 안된 오류
        3: 존재하지 않는 리소스에 대한 접근 오류
        4: 외부 API 관련 오류
        5: 기타 오류

    */

    TEST_ERROR(400, "9999", "테스트 에러 처리"),

    INVALID_ACCESS_TOKEN(401, "0200", "ACCESS TOKEN 인증 오류"),
    INVALID_REFRESH_TOKEN(401, "0201", "REFRESH TOKEN 인증 오류"),
    TOKEN_REGENERATE(401, "0202", "TOKEN 재발급 완료"),
    NO_AUTHORITY(401, "0203", "권한이 없습니다"),

    NOT_UNIQUE_EMAIL(400, "1100", "이메일이 이미 존재합니다."),
    NOT_SAME_PASSWORD(400, "1101", "비밀번호가 다릅니다."),
    INVALID_PASSWORD_PATTERN(400, "1102", "비밀번호는 대문자, 소문자, 숫자, 특수 문자가 하나 이상 포함되어야 합니다."),
    INVALID_PASSWORD(400, "1103", "비밀 번호가 틀렸습니다."),
    NOT_UNIQUE_NICKNAME(400, "1104", "닉네임이 이미 존재합니다."),
    NOT_CORRECT_AUTH_CODE(400, "1105", "인증번호가 틀립니다."),
    FAIL_SEND_AUTH_MESSAGE(400, "1106", "사용자 인증 번호 전송에 실패하였습니다."),
    SAME_PASSWORD_RESET(400, "1106", "이전 비밀번호와 동일합니다."),
    NOT_FOUND_EMAIL(404, "1300", "해당 이메일이 존재하지 않습니다."),
    NOT_FOUND_USER(404, "1301", "해당 사용자가 존재하지 않습니다."),
    NOT_FOUND_NICKNAME(404, "1302", "해당 닉네임이 존재하지 않습니다."),
    NOT_FOUND_ADMIN(404, "1304", "해당 관리자가 존재하지 않습니다."),

    NOT_UNIQUE_TEAM_NAME(400, "2100", "팀 이름이 이미 존재합니다."),
    NOT_FOUND_TEAM(400, "2300", "해당 팀이 존재하지 않습니다."),
    
    ALREADY_TEAM_MEMBER(400, "3100", "이미 등록된 멤버입니다."),
    ALREADY_NOT_MEMBER(400, "3101", "이미 탈퇴한 멤버입니다."),
    NO_TEAM_AUTHORITY(401, "3200", "해당 팀에서 권한이 없습니다."),
    NOT_FOUND_TEAM_MEMBER(404, "3300", "해당 소속팀 정보가 존재하지 않습니다."),

    NOT_UNIQUE_MATCHING_NAME(400, "4100", "경기 이름이 이미 존재합니다."),
    NOT_PERSONAL_MATCHING(400, "4101", "해당 경기는 개인 참여가 불가능합니다."),
    NOT_TEAM_MATCHING(400, "4102", "해당 경기는 팀 참여가 불가능합니다."),
    EMPTY_TEAM_NAME(400, "4103", "팀 경기의 팀 이름이 입력되지 않았습니다."),
    TEAM_MAX_MEMBER_NUM(400, "4104", "팀 경기의 모집 인원이 팀의 인원보다 적습니다. 모집 인원을 늘려주세요."),
    INVALID_TEAM_MATCH_INFO(400, "4105", "잘못된 팀 경기입니다. 고객센터에 문의해주세요"),
    NOT_FOUND_MATCHING(404, "4300", "해당 경기가 존재하지 않습니다."),

    ALREADY_JOIN_PERSONAL_MATCH(404, "5100", "해당 선수가 이미 경기에 참가중입니다."),
    NOT_FOUND_PERSONAL_MATCH_JOIN(404, "5300", "해당 개인 경기 참가 정보가 존재하지 않습니다."),

    ALREADY_JOIN_TEAM_MATCH(404, "6100", "해당 팀의 팀원 중 이미 경기에 참가중인 사용자가 있습니다."),
    NOT_FOUND_TEAM_MATCH_JOIN(404, "6300", "해당 팀 경기 참가 정보가 존재하지 않습니다."),

    NOT_FOUND_RECRUITMENT(404, "7300", "해당 모집글이 존재하지 않습니다."),

    WAITING_TEAM_APPLICATION(400, "8100", "이미 가입 신청 후 대기중입니다."),
    TEAM_APPLICATION_REFUSE_COLL_DOWN(400, "8101", "가입 신청이 거부된 후 1일 이상이 지나야합니다."),
    NOT_FOUND_TEAM_APPLICATION(400, "8300", "해당 가입 신청이 존재하지 않습니다."),

    NOT_FOUND_REGION(404, "9300", "해당 지역이 존재하지 않습니다."),
    NOT_FOUND_REGION_STRING(404, "9301", "해당 지역 문자열에 해당하는 지역이 존재하지 않습니다."),

    NOT_FOUND_STADIUM(404, "10300", "해당 경기장이 존재하지 않습니다."),
    NOT_FOUND_STADIUM_NAME(404, "10301", "해당 이름의 경기장이 존재하지 않습니다."),

    INVALID_DATA_PATTERN(400, "12000", "입력 값의 형식이 맞지 않습니다."),
    MISSING_REQUEST_HEADER(400, "12001", "요청 헤더에 빈 값이 있습니다."),
    FILE_EXIST(400, "12100", "파일이 존재합니다."),
    FILE_SAVE_FAIL(400, "12101", "파일 저장을 실패했습니다."),
    FILE_IMPORT_IMAGE(400, "12300", "이미지를 불러오는데 실패했습니다."),
    NO_HANDLED_EXCEPTION(400, "12500", "에러 발생. 고객 센터에 문의하세요.");

    private final int httpStatus;
    private final String code;
    private final String message;
}
