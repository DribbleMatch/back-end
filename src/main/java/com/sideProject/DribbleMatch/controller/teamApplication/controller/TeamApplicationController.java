package com.sideProject.DribbleMatch.controller.teamApplication.controller;

import com.sideProject.DribbleMatch.service.teamApplication.TeamApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teamApplication/page")
public class TeamApplicationController {

    private final TeamApplicationService teamApplicationService;

    @GetMapping("/{teamId}")
    public String getApplicationList(Model model, @PathVariable Long teamId) {
        model.addAttribute("teamApplicationList", teamApplicationService.findTeamApplicationsByTeam(teamId));
        return "/team/teamDetail :: #request-list-popup";
    }
}
