package com.sideProject.DribbleMatch.controller.team;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.service.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    //test 후 삭제
    @GetMapping("/test")
    public String test(Principal principal) {
        return principal.getName();
    }

    @PostMapping
    public ApiResponse<Long> createTeam(Principal principal, @RequestBody @Valid TeamCreateRequestDto request) {
        return ApiResponse.ok(teamService.createTeam(Long.valueOf(principal.getName()), request));
    }

    @PutMapping("/{teamId}")
    public ApiResponse<Long> updateTeam(@PathVariable("teamId") Long teamId, @RequestBody @Valid TeamUpdateRequestDto request) {
        return ApiResponse.ok(teamService.updateTeam(teamId, request));
    }

    @DeleteMapping("/{teamId}")
    public ApiResponse<String> deleteTeam(@PathVariable("teamId")  Long teamId) {
        return ApiResponse.ok(teamService.deleteTeam(teamId));
    }

    @GetMapping("/teams")
    public ApiResponse<Page<TeamResponseDto>> findAllTeams(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "regionString") String regionString) {
        return ApiResponse.ok(teamService.findAllTeams(pageable, regionString));
    }

    @GetMapping("/{teamId}")
    public ApiResponse<TeamResponseDto> findTeam(@PathVariable("teamId") Long teamId) {
        return ApiResponse.ok(teamService.findTeam(teamId));
    }
}
