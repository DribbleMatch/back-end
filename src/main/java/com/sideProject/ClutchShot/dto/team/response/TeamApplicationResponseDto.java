package com.sideProject.ClutchShot.dto.team.response;

import com.sideProject.ClutchShot.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.ClutchShot.entity.teamApplication.TeamApplication;
import lombok.Builder;

public class TeamApplicationResponseDto {
    Long id;
    Long userId;
    String nickName;
    String introduce;
    ApplicationStatus status;

    @Builder
    public TeamApplicationResponseDto(Long id, Long userId, String nickName, String introduce, ApplicationStatus status) {
        this.id = id;
        this.userId = userId;
        this.nickName = nickName;
        this.introduce = introduce;
        this.status = status;
    }

    public static TeamApplicationResponseDto of(TeamApplication teamApplication) {
        return TeamApplicationResponseDto.builder()
                .id(teamApplication.getId())
                .userId(teamApplication.getUser().getId())
                .nickName(teamApplication.getUser().getNickName())
                .introduce(teamApplication.getIntroduce())
                .status(teamApplication.getStatus())
                .build();
    }
}
