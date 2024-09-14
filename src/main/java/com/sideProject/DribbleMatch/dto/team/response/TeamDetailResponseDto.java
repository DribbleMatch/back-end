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
public class TeamDetailResponseDto {
    private Long id;
    private String imagePath;
    private String name;
    private double winningPercent;
    private int maxNum;
    private String leaderNickName;
    private String regionString;
    private String info;
    private List<TeamTag> tags;
    private List<UserTeamResponseDto> userList;

    @Builder
    public TeamDetailResponseDto(Long id,
                                 String imagePath,
                                 String name,
                                 double winningPercent,
                                 int maxNum,
                                 String leaderNickName,
                                 String regionString,
                                 String info,
                                 List<TeamTag> tags,
                                 List<UserTeamResponseDto> userList) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.maxNum = maxNum;
        this.winningPercent = winningPercent;
        this.leaderNickName = leaderNickName;
        this.regionString = regionString;
        this.info = info;
        this.tags = tags;
        this.userList = userList;
    }

    public static TeamDetailResponseDto of(Team team, String regionString) {
        return TeamDetailResponseDto.builder()
                .name(team.getName())
                .winningPercent(team.getWinning())
                .leaderNickName(team.getLeader().getNickName())
                .regionString(regionString)
                .build();
    }
}
