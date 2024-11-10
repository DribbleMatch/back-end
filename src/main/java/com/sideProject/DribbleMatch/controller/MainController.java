package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.dto.matching.response.MatchingDetailTestResponseDto;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page")
public class MainController {

    private final MatchingService matchingService;

    @GetMapping
    public String index(ModelMap model,
                        @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<MatchingDetailTestResponseDto> matchingList = matchingService.searchMatchings("", pageable, LocalDate.now());

        model.addAttribute("boardList", new ArrayList<>());
        model.addAttribute("recentMatchingList", matchingService.getRecentMatchingList());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());

        return "index";
    }

    @GetMapping("/openLater")
    public String openLater(ModelMap model) {

        return "openLater";
    }
}