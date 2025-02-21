package com.sideProject.ClutchShot.service.teamMember;

import com.sideProject.ClutchShot.entity.teamMember.ENUM.TeamRole;

import java.util.List;

public interface TeamMemberService {
    public Long createTeamMember(Long userId, Long teamId, TeamRole teamRole);
    public List<String> getTeamNameListByUserId(Long userId);
    public TeamRole getTeamRole(Long userId, Long teamId);
}
