package com.sideProject.DribbleMatch.domain.team.dto;

import com.sideProject.DribbleMatch.domain.team.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamResponseDto {
    private String name;
    private String region;
    private int winning;
    private String leaderNickName;

    @Builder
    public TeamResponseDto(String name, String region, int winning, String leaderNickName) {
        this.name = name;
        this.region = region;
        this.winning = winning;
        this.leaderNickName = leaderNickName;
    }

    public static TeamResponseDto of(Team team) {
        return TeamResponseDto.builder()
                .name(team.getName())
                .region(team.getRegion())
                .winning(team.getWinning())
                .leaderNickName(team.getLeader().getNickName())
                .build();
    }
}
