package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamApplicationServiceImpl implements TeamApplicationService{

    private final TeamApplicationRepository teamApplicationRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final TeamMemberService teamMemberService;

    @Override
    public Long apply(Long userId, TeamJoinRequestDto request) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        teamMemberService.checkAlreadyMember(user, team);

        TeamApplication teamApplication = teamApplicationRepository.save(TeamJoinRequestDto.toEntity(user, team, request.getIntroduce()));

        return teamApplication.getId();
    }

    @Override
    public Long approve(Long userId, Long teamApplicationId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        TeamApplication teamApplication = teamApplicationRepository.findById(teamApplicationId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_APPLICATION_ID));

        // 권한 검증
        teamMemberService.checkRole(user, teamApplication.getTeam());

        // 승인
        teamApplication.approve();

        return teamMemberService.join(teamApplication.getUser(), teamApplication.getTeam());
    }

    @Override
    public Long refuse(Long userId, Long teamApplicationId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        TeamApplication teamApplication = teamApplicationRepository.findById(teamApplicationId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_APPLICATION_ID));

        // 권한 검증
        teamMemberService.checkRole(user, teamApplication.getTeam());

        // 거절
        teamApplication.refuse();

        return teamApplication.getId();
    }
}
