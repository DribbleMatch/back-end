package com.sideProject.DribbleMatch.controller.matching.controller;

import com.sideProject.DribbleMatch.dto.matching.response.MatchingResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import com.sideProject.DribbleMatch.service.team.TeamService;
import com.sideProject.DribbleMatch.service.teamMember.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/matching/page")
public class MatchingController {

    private final RegionRepository regionRepository;
    private final MatchingService matchingService;
    private final TeamMemberService teamMemberService;

    @GetMapping("/createMatching")
    public String createMatchingPage(Principal principal, Model model) {
        model.addAttribute("siDoList", regionRepository.findAllSiDo());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("teamList", teamMemberService.selectTeamNameByUserId(Long.valueOf(principal.getName())));
        return "matching/createMatching";
    }

    @GetMapping("/matchingList")
    public String matchingListPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        model.addAttribute("today", LocalDate.now());

        Page<MatchingResponseDto> matchingList = matchingService.selectAllMatchingBySearchWordAndDateOrderByTimeInTime("", pageable, LocalDate.now());
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        return "matching/matchingList";
    }

    @PostMapping("/matchingList")
    public String matchingListDateAndSearch(Model model,
                                            @PageableDefault(page = 0, size = 10) Pageable pageable,
                                            @RequestParam LocalDate date,
                                            @RequestParam String searchWord) {

        Page<MatchingResponseDto> matchingList = matchingService.selectAllMatchingBySearchWordAndDateOrderByTimeInTime(searchWord, pageable, date);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        return "matching/matchingList :: #matching-list";
    }

    @GetMapping("/matchingDetail/{matchingId}")
    public String matchingDetailPage(Principal principal, Model model, @PathVariable Long matchingId) {
        model.addAttribute("matchingDetail", matchingService.findMatching(matchingId));
        model.addAttribute("teamList", teamMemberService.selectTeamNameByUserId(Long.valueOf(principal.getName())));
        return "matching/matchingDetail";
    }
}
