package com.sideProject.DribbleMatch.service.teamMember;

import com.sideProject.DribbleMatch.dto.team.request.ChangeTeamRoleRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.user.User;

import java.util.List;

public interface TeamMemberService {
    public Long createTeamMember(Long userId, Long teamId, TeamRole teamRole);
    public List<String> getTeamNameListByUserId(Long userId);
    public TeamRole getTeamRole(Long userId, Long teamId);
}
