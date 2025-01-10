package com.sideProject.DribbleMatch.service.componentService.teamMemberTeam;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;

public interface TeamMemberTeamService {
    public Long createTeam(Long creatorId, TeamCreateRequestDto request);
}
