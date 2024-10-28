package com.sideProject.DribbleMatch.dto.team.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamUpdateRequestDto {
    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;
    @NotNull(message = "지역이 입력되지 않았습니다.")
    private String regionString;
    @NotNull(message = "팀장 아이디가 입력되지 않았습니다.")
    private Long leaderId;
    @NotNull(message = "최대 인원 수가 입력되지 않았습니다.")
    private Long maxNum;

    @Builder
    public TeamUpdateRequestDto(String name, String regionString, Long leaderId, Long maxNum) {
        this.name = name;
        this.regionString = regionString;
        this.leaderId = leaderId;
        this.maxNum = maxNum;
    }
}
