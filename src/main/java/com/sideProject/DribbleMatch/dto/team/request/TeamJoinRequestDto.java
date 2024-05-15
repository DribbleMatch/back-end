package com.sideProject.DribbleMatch.dto.team.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamJoinRequestDto {
    String introduce;

    @Builder
    public TeamJoinRequestDto(String introduce) {
        this.introduce = introduce;
    }
}
