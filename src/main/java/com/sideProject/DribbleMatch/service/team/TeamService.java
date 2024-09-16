package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamApplicationResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamMemberResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamDetailResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamService {
    //팀 생성 및 관리
    public Long createTeam(Long createId, TeamCreateRequestDto request);
    public void checkTeamName(String name);
    public TeamDetailResponseDto selectTeam(Long teamId);
    public List<TeamListResponseDto> selectAllTeam();
    public List<TeamListResponseDto> selectAllTeamByUserId(Long userId);
    public List<TeamListResponseDto> selectAllTeamBySearchWord(String searchWord);
}
