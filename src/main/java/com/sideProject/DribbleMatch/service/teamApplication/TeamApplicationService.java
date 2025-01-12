package com.sideProject.DribbleMatch.service.teamApplication;

import com.sideProject.DribbleMatch.dto.teamApplication.TeamApplicationListResponseDto;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;

import java.util.List;

public interface TeamApplicationService {
    public Long createTeamApplication(Long userId, Long teamId, String introduce);
    public List<TeamApplicationListResponseDto> getTeamApplicationListByTeam(Long teamId);
    public Long changeTeamApplicationStatus(Long teamApplicationId, ApplicationStatus status);
}
