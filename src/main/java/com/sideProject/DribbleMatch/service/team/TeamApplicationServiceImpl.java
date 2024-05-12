package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamApplicationServiceImpl implements TeamApplicationService{

    private final TeamApplicationRepository teamApplicationRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public Long approve(Long joinId, Long userId) {

        //todo: 유저와 팀의 조회 여부 생각
        TeamApplication teamApplication = teamApplicationRepository.findById(joinId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_APPLICATION));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        if(teamMemberRepository.findByUserAndTeam(teamApplication.getUser(),teamApplication.getTeam()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_MEMBER);
        }

        TeamMember adminTeamMember = teamMemberRepository.findByUserAndTeam(user,teamApplication.getTeam()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER));
        if(!adminTeamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        //승인
        teamApplication.approve();
        teamApplicationRepository.save(teamApplication);

        TeamMember newMember = teamMemberRepository.save(TeamMember.builder()
                .user(teamApplication.getUser())
                .team(teamApplication.getTeam())
                .build());

        return newMember.getId();
    }

    @Override
    public Long refuse(Long joinId, Long userId) {
        //todo: 유저와 팀의 조회 여부 생각
        TeamApplication teamApplication = teamApplicationRepository.findById(joinId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_APPLICATION));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        teamMemberRepository.findByUserAndTeam(teamApplication.getUser(),teamApplication.getTeam()).orElseThrow(() ->
                new CustomException(ErrorCode.ALREADY_MEMBER));

        TeamMember adminTeamMember = teamMemberRepository.findByUserAndTeam(user,teamApplication.getTeam()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER));
        if(!adminTeamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        //승인
        teamApplication.refuse();
        teamApplicationRepository.save(teamApplication);

        return teamApplication.getId();
    }
}
