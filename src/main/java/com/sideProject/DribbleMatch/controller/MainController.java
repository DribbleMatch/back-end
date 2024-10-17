package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page")
public class MainController {

    private final MatchingService matchingService;

    @GetMapping
    public String index(ModelMap model) {

        LocalDate nowDate = LocalDate.now();
        model.addAttribute("nowDate", nowDate);

        model.addAttribute("matchingList", null);

        return "index";
    }

    @GetMapping("/login")
    public String login(ModelMap model) {

        return "login/login";
    }

    @GetMapping("/openLater")
    public String openLater(ModelMap model) {

        return "openLater";
    }
}