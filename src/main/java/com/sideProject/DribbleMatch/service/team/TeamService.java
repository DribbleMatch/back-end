package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamApplicationResponseDto;
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

    //todo: 팀원 관리 (분리 한다면 여기?)
    public Long join(TeamJoinRequestDto request, Long teamId, Long userId);
    public Long cancel(Long joinId, Long userId);
    Page<TeamApplicationResponseDto> findApplication(Pageable pageable, Long teamId);
    public Long approve(Long joinId, Long userId);
    public Long refuse(Long joinId, Long userId);
    public Long withdraw(Long memberId, Long teamId, Long userId);
    public List<TeamMemberResponseDto> findMember(Long teamId);
}
