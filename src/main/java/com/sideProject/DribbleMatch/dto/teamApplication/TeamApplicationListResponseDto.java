package com.sideProject.DribbleMatch.dto.teamApplication;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamApplicationListResponseDto {

    private Long id;
    private String nickName;
    private String positionString;
    private int level;
    private String introduce;

    @Builder
    public TeamApplicationListResponseDto(Long id, String nickName, String positionString, int level, String introduce) {
        this.id = id;
        this.nickName = nickName;
        this.positionString = positionString;
        this.level = level;
        this.introduce = introduce;
    }
}
