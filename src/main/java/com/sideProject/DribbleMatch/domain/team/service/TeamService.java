package com.sideProject.DribbleMatch.domain.team.service;

import com.sideProject.DribbleMatch.domain.team.dto.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.domain.team.dto.TeamResponseDto;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    public Long createTeam(Long createId, TeamCreateRequestDto request);
    public Long updateTeam(Long teamId, TeamUpdateRequestDto request);
    public String deleteTeam(Long teamId);
    public Page<TeamResponseDto> findAllTeams(Pageable pageable, String region);
    public TeamResponseDto findTeam(Long teamId);
}
