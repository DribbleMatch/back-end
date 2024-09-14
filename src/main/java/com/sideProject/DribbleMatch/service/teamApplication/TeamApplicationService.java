package com.sideProject.DribbleMatch.service.teamApplication;

import com.sideProject.DribbleMatch.dto.teamApplication.TeamApplicationListResponseDto;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;

import java.util.List;

public interface TeamApplicationService {

    public void applyTeam(Long userId, Long teamId, String introduce);
    public List<TeamApplication> findTeamApplicationsByUser(Long userId);
    public List<TeamApplicationListResponseDto> findTeamApplicationsByTeam(Long teamId);
    public void changeTeamApplicationStatus(Long teamApplicationId, ApplicationStatus status);
    public void requestJoin(Long userId, Long teamId, String introduce);
}
