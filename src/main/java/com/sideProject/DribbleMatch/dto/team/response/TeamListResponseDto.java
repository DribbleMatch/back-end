package com.sideProject.DribbleMatch.dto.team.response;

import com.sideProject.DribbleMatch.dto.user.response.UserTeamResponseDto;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamTag;
import com.sideProject.DribbleMatch.entity.team.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamListResponseDto {
    private Long id;
    private String imagePath;
    private String name;
    private String regionString;
    private String memberNumString;
    private double winningPercent;

    @Builder
    public TeamListResponseDto(Long id,
                               String imagePath,
                               String name,
                               String regionString,
                               String memberNumString,
                               double winningPercent) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.regionString = regionString;
        this.memberNumString = memberNumString;
        this.winningPercent = winningPercent;
    }
}
