package com.sideProject.DribbleMatch.dto.team.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamJoinRequestDto {
    Long teamId;
    String introduce;

    @Builder
    public TeamJoinRequestDto(Long teamId, String introduce) {
        this.teamId = teamId;
        this.introduce = introduce;
    }
}
