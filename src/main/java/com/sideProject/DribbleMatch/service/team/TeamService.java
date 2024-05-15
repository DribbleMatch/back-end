package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamMemberResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamService {
    //팀 생성 및 관리
    public Long createTeam(Long createId, TeamCreateRequestDto request);
    public Long updateTeam(Long userId, Long teamId, TeamUpdateRequestDto request);
    public String deleteTeam(Long userId, Long teamId);
    public Page<TeamResponseDto> findAllTeams(Pageable pageable, String regionString);
    public TeamResponseDto findTeam(Long teamId);
}
