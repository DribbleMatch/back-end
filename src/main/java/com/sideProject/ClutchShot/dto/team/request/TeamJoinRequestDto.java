package com.sideProject.ClutchShot.dto.team.request;

import com.sideProject.ClutchShot.entity.team.Team;
import com.sideProject.ClutchShot.entity.teamApplication.TeamApplication;
import com.sideProject.ClutchShot.entity.user.User;
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

    public static TeamApplication toEntity(User user, Team team, String introduce) {
        return TeamApplication.builder()
                .user(user)
                .team(team)
                .introduce(introduce)
                .build();
    }
}
