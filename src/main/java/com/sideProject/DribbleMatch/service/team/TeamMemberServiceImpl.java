package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.team.request.ChangeTeamRoleRequestDto;
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
public class TeamMemberServiceImpl implements TeamMemberService{

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public Long join(User member, Team team) {

        checkAlreadyMember(member, team);

        TeamMember newMember = teamMemberRepository.save(TeamMember.builder()
                .user(member)
                .team(team)
                .teamRole(TeamRole.MEMBER)
                .build());

        return newMember.getId();
    }

    @Override
    public Long withdraw(Long adminId, Long memberId, Long teamId) {

        User adminUser = userRepository.findById(adminId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        User memberUser = userRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        checkRole(adminUser, team);

        TeamMember teamMember = teamMemberRepository.findByUserAndTeam(memberUser,team).orElseThrow(() ->
                new CustomException(ErrorCode.ALREADY_NOT_MEMBER));

        teamMemberRepository.delete(teamMember);

        return teamMember.getId();
    }

    public Long changeTeamRole(Long adminId, ChangeTeamRoleRequestDto request) {

        User admin = userRepository.findById(adminId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        TeamMember teamMember = teamMemberRepository.findById(request.getTeamMemberId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER_ID));

        checkRole(admin, teamMember.getTeam());

        teamMember.changeTeamRole(request.getTeamRole());

        return teamMember.getId();
    }

    @Override
    public void checkRole(User user, Team team) {

        TeamMember teamMember = teamMemberRepository.findByUserAndTeam(user, team).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER));

        if(teamMember.getTeamRole() != TeamRole.ADMIN) {
            throw new CustomException(ErrorCode.NO_TEAM_AUTHORITY);
        }
    }

    @Override
    public void checkAlreadyMember(User user, Team team) {

        if(teamMemberRepository.findByUserAndTeam(user, team).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_MEMBER);
        }
    }
}
