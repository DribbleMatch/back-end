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
public class TeamMemberServiceImpl implements TeamMemberService{

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamApplicationRepository teamApplicationRepository;

    @Override
    public Long join(TeamJoinRequestDto request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        Optional<TeamMember> teamMember = teamMemberRepository.findByUserAndTeam(user,team);
        if(teamMember.isPresent()) {
            throw  new CustomException(ErrorCode.ALREADY_MEMBER);
        }

        TeamApplication teamApplication = teamApplicationRepository.save(TeamApplication.builder()
                .team(team)
                .user(user)
                .introduce(request.getIntroduce())
                .build()
        );
        return teamApplication.getId();
    }

    @Override
    public Long withdraw(Long memberId, Long teamId, Long adminId) {

        User adminUser = userRepository.findById(adminId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        User memberUser = userRepository.findById(memberId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        TeamMember adminMember = teamMemberRepository.findByUserAndTeam(adminUser,team).orElseThrow(() ->
                new CustomException(ErrorCode.ALREADY_MEMBER));
        if(!adminMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        TeamMember member = teamMemberRepository.findByUserAndTeam(memberUser,team).orElseThrow(() ->
                new CustomException(ErrorCode.ALREADY_MEMBER));

        teamMemberRepository.delete(member);

        return member.getId();
    }
}
