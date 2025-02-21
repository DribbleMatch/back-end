package com.sideProject.ClutchShot.service.componentService.teamMemberTeam;

import com.sideProject.ClutchShot.dto.team.request.TeamCreateRequestDto;
import com.sideProject.ClutchShot.entity.teamMember.ENUM.TeamRole;
import com.sideProject.ClutchShot.service.team.TeamService;
import com.sideProject.ClutchShot.service.teamMember.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberTeamServiceImpl implements TeamMemberTeamService{

    private final TeamService teamService;
    private final TeamMemberService teamMemberService;

    @Override
    @Transactional
    public Long createTeam(Long creatorId, TeamCreateRequestDto request) {

        Long teamId = teamService.createTeam(creatorId, request);

        teamMemberService.createTeamMember(creatorId, teamId, TeamRole.ADMIN);

        return teamId;
    }
}
