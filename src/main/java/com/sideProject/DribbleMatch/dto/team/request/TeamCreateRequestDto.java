package com.sideProject.DribbleMatch.dto.team.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCreateRequestDto {
    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;
    @NotNull(message = "지역이 입력되지 않았습니다.")
    private String regionString;

    @Builder
    public TeamCreateRequestDto(String name, String regionString) {
        this.name = name;
        this.regionString = regionString;
    }
}
