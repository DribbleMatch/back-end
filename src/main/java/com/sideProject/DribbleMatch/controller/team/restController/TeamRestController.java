package com.sideProject.DribbleMatch.controller.team.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.service.componentService.teamMemberTeam.TeamMemberTeamService;
import com.sideProject.DribbleMatch.service.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamRestController {

    private final TeamService teamService;
    private final TeamMemberTeamService teamMemberTeamService;

    @PostMapping("/checkTeamName")
    public ApiResponse<String> checkTeamName(@RequestParam(name = "name") String name) {

        teamService.checkTeamName(name);

        return ApiResponse.ok("팀 이름 사용 가능");
    }

    @PostMapping("/createTeam")
    public ApiResponse<Long> createTeam(Principal principal, @ModelAttribute TeamCreateRequestDto request) {

        return ApiResponse.ok(teamMemberTeamService.createTeam(Long.valueOf(principal.getName()), request));
    }
}
