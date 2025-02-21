package com.sideProject.ClutchShot.controller.matching.controller;

import com.sideProject.ClutchShot.common.util.CommonUtil;
import com.sideProject.ClutchShot.dto.matching.response.MatchingDetailTestResponseDto;
import com.sideProject.ClutchShot.dto.matching.response.MatchingSimpleResponseDto;
import com.sideProject.ClutchShot.entity.matching.ENUM.GameKind;
import com.sideProject.ClutchShot.repository.region.RegionRepository;
import com.sideProject.ClutchShot.service.matching.MatchingService;
import com.sideProject.ClutchShot.service.teamMember.TeamMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page/matching")
public class MatchingController {

    private final RegionRepository regionRepository;
    private final MatchingService matchingService;
    private final TeamMemberService teamMemberService;

    @GetMapping("/create")
    public String createMatchingPage(HttpServletRequest request,
                                     Model model,
                                     Principal principal) {

        model.addAttribute("siDoList", regionRepository.findAllSiDo());
        model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
        model.addAttribute("teamList", teamMemberService.getTeamNameListByUserId(Long.valueOf(principal.getName())));
        if (request.getAttribute("haveInput").equals(1)) {
            model.addAttribute("inputList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        }

        return "matching/createMatching";
    }

    @GetMapping("/matchingList")
    public String matchingListPage(HttpServletRequest request,
                                   Model model,
                                   Principal principal,
                                   @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<MatchingSimpleResponseDto> matchingList = matchingService.searchMatchings("", pageable, LocalDate.now());

        model.addAttribute("dateList", CommonUtil.getDateList(LocalDate.now()));
        model.addAttribute("mobileDateList", IntStream.range(0, 14)
                .mapToObj(i -> LocalDate.now().plusDays(i))
                .collect(Collectors.toList()));
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        if (request.getAttribute("haveInput").equals(1)) {
            model.addAttribute("inputList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        }

        return "matching/matchingList";
    }

    @PostMapping("/replace/matchingList")
    public String replaceMatchingListByDateAndSearch(HttpServletRequest request,
                                                     Model model,
                                                     Principal principal,
                                                     @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                     @RequestParam(name = "date") LocalDate date,
                                                     @RequestParam(name = "searchWord") String searchWord) {

        Page<MatchingSimpleResponseDto> matchingList = matchingService.searchMatchings(searchWord, pageable, date);

        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        if (request.getAttribute("haveInput").equals(1)) {
            model.addAttribute("inputList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        }

        return "matching/matchingList :: #matching-list";
    }

    @GetMapping("/detail/{matchingId}")
    public String matchingDetailPage(HttpServletRequest request,
                                     Model model,
                                     Principal principal,
                                     @PathVariable Long matchingId) {

        model.addAttribute("matchingDetail", matchingService.getMatchingDetail(matchingId));
        model.addAttribute("teamList", teamMemberService.getTeamNameListByUserId(Long.valueOf(principal.getName())));
        if (request.getAttribute("haveInput").equals(1)) {
            model.addAttribute("inputList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        }

        return "matching/matchingDetail";
    }

    // 마이페이지
    @GetMapping("/reservedMatchingList/{gameKind}")
    public String reservedMatchingListPage(HttpServletRequest request,
                                           Model model,
                                           Principal principal,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable,
                                           @PathVariable GameKind gameKind) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getReservedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        if (request.getAttribute("haveInput").equals(1)) {
            model.addAttribute("inputList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        }

        return "myPage/reservedMatchingList";
    }

    @GetMapping("/replace/reservedMatchingList/{gameKind}")
    public String replaceReservedMatchingList(HttpServletRequest request,
                                              Model model,
                                              Principal principal,
                                              @PageableDefault(page = 0, size = 10) Pageable pageable,
                                              @PathVariable GameKind gameKind) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getReservedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        return "myPage/reservedMatchingList :: #matching-list";
    }

    @GetMapping("/endedMatchingList/{gameKind}")
    public String endedMatchingListPage(HttpServletRequest request,
                                        Model model,
                                        Principal principal,
                                        @PageableDefault(page = 0, size = 10) Pageable pageable,
                                        @PathVariable GameKind gameKind) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getEndedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        if (request.getAttribute("haveInput").equals(1)) {
            model.addAttribute("inputList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        }

        return "myPage/endedMatchingList";
    }

    @GetMapping("/replace/endedMatchingList/{gameKind}")
    public String replaceEndedMatchingList(HttpServletRequest request,
                                           Model model,
                                           Principal principal,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable,
                                           @PathVariable GameKind gameKind) {
        Page<MatchingDetailTestResponseDto> matchingList = matchingService.getEndedMatchingList(Long.valueOf(principal.getName()), gameKind, pageable);

        model.addAttribute("gameKind", gameKind);
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());
        if (request.getAttribute("haveInput").equals(1)) {
            model.addAttribute("inputList", matchingService.getNotInputScoreMatchingList(Long.valueOf(principal.getName())));
        }

        return "myPage/endedMatchingList :: #matching-list";
    }
}
