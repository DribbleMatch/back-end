package com.sideProject.DribbleMatch.controller.team.controller;

import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamTag;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.teamMember.TeamMemberService;
import com.sideProject.DribbleMatch.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page/team")
public class TeamController {

    private final RegionRepository regionRepository;
    private final TeamService teamService;
    private final TeamMemberService teamMemberService;

    @GetMapping("/createTeam")
    public String createTeamPage(Model model) {

        model.addAttribute("siDoList", regionRepository.findAllSiDo());
        model.addAttribute("tagList", TeamTag.values());

        return "team/createTeam";
    }
    @GetMapping("/teamList")
    public String teamList(Principal principal,
                           Model model,
                           @PageableDefault(page = 0, size = 10) Pageable pageable,
                           @RequestParam(name = "myPage", required = false, defaultValue = "0") int myPage) {

        Page<TeamListResponseDto> teamList = teamService.searchTeamsBySearchWord("", pageable);

        if (myPage == 1) {
            teamList = teamService.searchTeamsByUserId(Long.valueOf(principal.getName()), pageable);
        }

        model.addAttribute("teamList", teamList);
        model.addAttribute("currentPage", teamList.getPageable().getPageNumber());
        model.addAttribute("totalPage", teamList.getTotalPages());

        return "team/teamList";
    }

    @PostMapping("/replace/teamList")
    public String replaceTeamListBySearch(Model model,
                                          @RequestParam(name = "searchWord") String searchWord,
                                          @PageableDefault(size = 10) Pageable pageable) {

        Page<TeamListResponseDto> teamList = teamService.searchTeamsBySearchWord(searchWord, pageable);

        model.addAttribute("teamList", teamList);
        model.addAttribute("currentPage", teamList.getPageable().getPageNumber());
        model.addAttribute("totalPage", teamList.getTotalPages());

        return "team/teamList :: #team-list";
    }

    @GetMapping("/replace/myTeamListView")
    public String replaceTeamListByMyTeam(Model model, Principal principal, @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<TeamListResponseDto> teamList = teamService.searchTeamsByUserId(Long.valueOf(principal.getName()), pageable);

        model.addAttribute("teamList", teamList);
        model.addAttribute("currentPage", teamList.getPageable().getPageNumber());
        model.addAttribute("totalPage", teamList.getTotalPages());

        return "team/teamList :: #team-list";
    }

    @GetMapping("/teamDetail/{teamId}")
    public String teamDetailPage(Model model, Principal principal, @PathVariable Long teamId) {

        model.addAttribute("team", teamService.getTeamDetail(teamId));
        model.addAttribute("teamRole", teamMemberService.getTeamRoe(Long.valueOf(principal.getName()), teamId));

        return "team/teamDetail";
    }
}
