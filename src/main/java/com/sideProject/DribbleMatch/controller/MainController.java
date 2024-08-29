package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MatchingService matchingService;

    @GetMapping("/")
    public String index(ModelMap model) {

        LocalDate nowDate = LocalDate.now();
        model.addAttribute("nowDate", nowDate);

        List<Matching> matchingList = matchingService.findAllMatchingOrderByTime(LocalDate.now());
        model.addAttribute("matchingList", matchingList);

        return "/index";
    }
}
