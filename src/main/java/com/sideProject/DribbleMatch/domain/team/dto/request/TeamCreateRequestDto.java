package com.sideProject.DribbleMatch.domain.team.dto.request;

import com.sideProject.DribbleMatch.domain.user.entity.User;
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
    private String region;

    @Builder
    public TeamCreateRequestDto(String name, String region) {
        this.name = name;
        this.region = region;
    }
}
