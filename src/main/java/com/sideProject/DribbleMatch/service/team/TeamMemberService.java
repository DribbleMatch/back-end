package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.User;

public interface TeamMemberService {
    public Long join(User member, Team team);
    public Long withdraw(Long adminId, Long memberId, Long teamId);
    public void checkRole(User user, Team team);
    public void checkAlreadyMember(User user, Team team);
}
