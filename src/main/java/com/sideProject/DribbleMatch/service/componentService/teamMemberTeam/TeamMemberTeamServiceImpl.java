package com.sideProject.DribbleMatch.service.componentService.teamMemberTeam;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.service.team.TeamService;
import com.sideProject.DribbleMatch.service.teamMember.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

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
