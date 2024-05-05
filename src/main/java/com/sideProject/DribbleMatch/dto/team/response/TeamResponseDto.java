package com.sideProject.DribbleMatch.dto.team.response;

import com.sideProject.DribbleMatch.entity.team.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamResponseDto {
    private String name;
    private int winning;
    private String leaderNickName;
    private String regionString;

    @Builder
    public TeamResponseDto(String name, int winning, String leaderNickName, String regionString) {
        this.name = name;
        this.winning = winning;
        this.leaderNickName = leaderNickName;
        this.regionString = regionString;
    }

    public static TeamResponseDto of(Team team, String regionString) {
        return TeamResponseDto.builder()
                .name(team.getName())
                .winning(team.getWinning())
                .leaderNickName(team.getLeader().getNickName())
                .regionString(regionString)
                .build();
    }
}
