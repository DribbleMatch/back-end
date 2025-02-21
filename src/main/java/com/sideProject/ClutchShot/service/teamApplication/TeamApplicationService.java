package com.sideProject.ClutchShot.service.teamApplication;

import com.sideProject.ClutchShot.dto.teamApplication.TeamApplicationListResponseDto;
import com.sideProject.ClutchShot.entity.teamApplication.ENUM.ApplicationStatus;

import java.util.List;

public interface TeamApplicationService {
    public Long createTeamApplication(Long userId, Long teamId, String introduce);
    public List<TeamApplicationListResponseDto> getTeamApplicationListByTeam(Long teamId);
    public Long changeTeamApplicationStatus(Long teamApplicationId, ApplicationStatus status);
}
