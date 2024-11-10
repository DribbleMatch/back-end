package com.sideProject.DribbleMatch.service.teamMember;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.team.request.ChangeTeamRoleRequestDto;
import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.teamMember.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberServiceImpl implements TeamMemberService{

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    @Transactional
    public Long createTeamMember(Long userId, Long teamId, TeamRole teamRole) {

        if (teamMemberRepository.findByUserIdAndTeamId(userId, teamId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_TEAM_MEMBER);
        }

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM));

        if (team.getMaxNumber() == teamMemberRepository.findByTeamId(teamId).size()) {
            throw new CustomException(ErrorCode.MAXIMUM_MEMBER);
        }

        return teamMemberRepository.save(TeamMember.builder()
                .user(user)
                .team(team)
                .teamRole(teamRole)
                .build()).getId();
    }

    @Override
    public List<String> getTeamNameListByUserId(Long userId) {
        return teamMemberRepository.findTeamNameByUserId(userId);
    }

    @Override
    public TeamRole getTeamRole(Long userId, Long teamId) {
        return teamMemberRepository.findByUserIdAndTeamId(userId, teamId)
                .map(TeamMember::getTeamRole)  // UserTeam이 존재하면 getTeamRole() 호출
                .orElse(null);  // 존재하지 않으면 null 반환
    }
}
