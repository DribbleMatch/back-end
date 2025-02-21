package com.sideProject.ClutchShot.service.team;

import com.sideProject.ClutchShot.dto.team.request.TeamCreateRequestDto;
import com.sideProject.ClutchShot.dto.team.response.TeamListResponseDto;
import com.sideProject.ClutchShot.dto.team.response.TeamDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    //팀 생성 및 관리
    public Long createTeam(Long createId, TeamCreateRequestDto request);
    public void checkTeamName(String name);
    public TeamDetailResponseDto getTeamDetail(Long teamId);
    public Page<TeamListResponseDto> searchTeamsByUserId(Long userId, Pageable pageable);
    public Page<TeamListResponseDto> searchTeamsBySearchWord(String searchWord, Pageable pageable);
}
