package com.sideProject.ClutchShot.service.componentService.teamMemberTeam;

import com.sideProject.ClutchShot.dto.team.request.TeamCreateRequestDto;

public interface TeamMemberTeamService {
    public Long createTeam(Long creatorId, TeamCreateRequestDto request);
}
