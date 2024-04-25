package com.sideProject.DribbleMatch.domain.team.service;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.domain.team.dto.TeamResponseDto;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.team.repository.TeamRepository;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import com.sideProject.DribbleMatch.domain.user.repository.UserRepository;
import com.sideProject.DribbleMatch.domain.userTeam.entity.UserTeam;
import com.sideProject.DribbleMatch.domain.userTeam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;

    @Override
    public Long createTeam(Long creatorId, TeamCreateRequestDto request) {

        checkUniqueName(request.getName());

        // 팀 생성 시에는 팀 생성한 회원이 팀장, 팀 정보 수정에서 변경 가능
        User creator = userRepository.findById(creatorId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        Team team = teamRepository.save(Team.builder()
                        .name(request.getName())
                        .region(request.getRegion())
                        .winning(0)
                        .leader(creator)
                        .build());

        userTeamRepository.save(UserTeam.builder()
                .team(team)
                .user(creator)
                .build());

        return team.getId();
    }

    @Override
    public Long updateTeam(Long teamId, TeamUpdateRequestDto request) {

        checkUniqueName(request.getName());

        User leader = userRepository.findById(request.getLeaderId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team teamToUpdate = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));
        teamToUpdate.updateTeam(request, leader);

        //todo: 변경된 리더가 팀원이 아닐 경우 에러 발생 처리
        return teamId;
    }

    @Override
    public String deleteTeam(Long teamId) {

        teamRepository.deleteById(teamId);

        return "팀이 삭제되었습니다.";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponseDto> findAllTeams(Pageable pageable, String region) {
        //todo: 지역별 조회 구현
        if (region.equals("ALL")) {
            Page<Team> teams = teamRepository.findAll(pageable);
            return teams.map(TeamResponseDto::of);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponseDto findTeam(Long teamId) {
        return TeamResponseDto.of(teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)));
    }

    private void checkUniqueName(String name) {
        if(teamRepository.findByName(name).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_TEAM_NAME);
        }
    }
}
