package com.sideProject.DribbleMatch.controller.team.controller;

import com.sideProject.DribbleMatch.entity.team.ENUM.TeamTag;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.teamMember.TeamMemberService;
import com.sideProject.DribbleMatch.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/team/page")
public class TeamController {

    private final RegionRepository regionRepository;
    private final TeamService teamService;
    private final TeamMemberService teamMemberService;

    @GetMapping("/createTeam")
    public String createTeamPage(Model model) {
        model.addAttribute("siDoList", regionRepository.findAllSiDo());
        model.addAttribute("tagList", TeamTag.values());
        return "/team/createTeam";
    }

    @GetMapping("/teamDetail/{teamId}")
    public String teamDetailPage(Model model, Principal principal, @PathVariable Long teamId) {
        model.addAttribute("team", teamService.selectTeam(teamId));
        model.addAttribute("teamRole", teamMemberService.getTeamRoe(Long.valueOf(principal.getName()), teamId));
        return "/team/teamDetail";
    }

    @GetMapping("/teamListView")
    public String teamListView(Model model) {
        model.addAttribute("teamList", teamService.selectAllTeam());
        return "/team/teamListView";
    }

    @PostMapping("/teamListView")
    public String teamListView(Model model, @RequestParam(name = "searchWord") String searchWord) {
        model.addAttribute("teamList", teamService.selectAllTeamBySearchWord(searchWord));
        return "/team/teamListView :: #team-list";
    }

    @GetMapping("/myTeamListView")
    public String myTeamListView(Model model, Principal principal) {
        model.addAttribute("teamList", teamService.selectAllTeamByUserId(Long.valueOf(principal.getName())));
        return "/team/teamListView :: #team-list";
    }
}
