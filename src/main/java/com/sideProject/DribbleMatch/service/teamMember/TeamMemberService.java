package com.sideProject.DribbleMatch.service.teamMember;

import com.sideProject.DribbleMatch.dto.team.request.ChangeTeamRoleRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.user.User;

public interface TeamMemberService {

    public TeamRole getTeamRoe(Long userId, Long teamId);
    public void joinTeam(Long userId, Long teamId);
}
