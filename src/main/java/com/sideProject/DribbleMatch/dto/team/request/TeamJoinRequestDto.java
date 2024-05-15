package com.sideProject.DribbleMatch.dto.team.request;

import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.JoinStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.User;
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

    public static TeamApplication toEntity(User user, Team team, String introduce) {
        return TeamApplication.builder()
                .user(user)
                .team(team)
                .introduce(introduce)
                .build();
    }
}
