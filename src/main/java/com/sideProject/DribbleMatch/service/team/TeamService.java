package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    public Long createTeam(Long createId, TeamCreateRequestDto request);
    public Long updateTeam(Long teamId, TeamUpdateRequestDto request);
    public String deleteTeam(Long teamId);
    public Page<TeamResponseDto> findAllTeams(Pageable pageable, String regionString);
    public TeamResponseDto findTeam(Long teamId);
}
