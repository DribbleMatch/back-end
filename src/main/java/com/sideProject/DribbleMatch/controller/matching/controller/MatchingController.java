package com.sideProject.DribbleMatch.controller.matching.controller;

import com.sideProject.DribbleMatch.dto.matching.response.MatchingDetailTestResponseDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
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
@RequestMapping("/page/matching")
public class MatchingController {

    private final RegionRepository regionRepository;
    private final MatchingService matchingService;
    private final TeamMemberService teamMemberService;

    @GetMapping("/createMatching")
    public String createMatchingPage(Principal principal, Model model) {

        model.addAttribute("siDoList", regionRepository.findAllSiDo());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("teamList", teamMemberService.getTeamNameListByUserId(Long.valueOf(principal.getName())));

        return "matching/createMatching";
    }

    @GetMapping("/matchingList")
    public String matchingListPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<MatchingDetailTestResponseDto> matchingList = matchingService.searchMatchings("", pageable, LocalDate.now());

        model.addAttribute("today", LocalDate.now());
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());

        return "matching/matchingList";
    }

    @PostMapping("/replace/matchingList")
    public String replaceMatchingListByDateAndSearch(Model model,
                                            @PageableDefault(page = 0, size = 10) Pageable pageable,
                                            @RequestParam(name = "date") LocalDate date,
                                            @RequestParam(name = "searchWord") String searchWord) {

        Page<MatchingDetailTestResponseDto> matchingList = matchingService.searchMatchings(searchWord, pageable, date);

        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());

        return "matching/matchingList :: #matching-list";
    }

    @GetMapping("/matchingDetail/{matchingId}")
    public String matchingDetailPage(Principal principal, Model model, @PathVariable Long matchingId) {

        model.addAttribute("matchingDetail", matchingService.getMatchingDetail(matchingId));
        model.addAttribute("teamList", teamMemberService.getTeamNameListByUserId(Long.valueOf(principal.getName())));

        return "matching/matchingDetail";
    }

    // 마이페이지
    @GetMapping("/reservedMatchingList/{gameKind}")
    public String reservedMatchingListPage(Model model,
                                           Principal principal,
                                           @PathVariable GameKind gameKind,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getReservedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        return "myPage/reservedMatchingList";
    }

    @GetMapping("/replace/reservedMatchingList/{gameKind}")
    public String replaceReservedMatchingList(Model model,
                                           Principal principal,
                                           @PathVariable GameKind gameKind,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getReservedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        return "myPage/reservedMatchingList :: #matching-list";
    }

    @GetMapping("/endedMatchingList/{gameKind}")
    public String endedMatchingListPage(Model model,
                                           Principal principal,
                                           @PathVariable GameKind gameKind,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getEndedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        return "myPage/endedMatchingList";
    }

    @GetMapping("/replace/endedMatchingList/{gameKind}")
    public String replaceEndedMatchingList(Model model,
                                              Principal principal,
                                              @PathVariable GameKind gameKind,
                                              @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getEndedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        return "myPage/endedMatchingList :: #matching-list";
    }

    @GetMapping("/inputScore")
    public String inputScorePage(Model model,
                                 Principal principal) {
        model.addAttribute("matchingList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        return "matching/inputScore";
    }
}
