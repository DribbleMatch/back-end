package com.sideProject.DribbleMatch.controller.teamMember.rest;

import com.sideProject.DribbleMatch.service.teamMember.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teamMember/rest")
public class TeamMemberRestController {

    private final TeamMemberService teamMemberService;

    @GetMapping("/{teamId}")
    public void joinTeam(Principal principal, @PathVariable Long teamId) {
        teamMemberService.joinTeam(Long.valueOf(principal.getName()), teamId);
    }
}
