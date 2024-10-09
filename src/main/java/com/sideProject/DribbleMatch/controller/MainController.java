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

        return "/index";
    }

    @GetMapping("/login")
    public String login(ModelMap model) {

        return "/login/login";
    }
}
//todo: 매핑 안된 URL에도 토큰 인증 실패 json이 가는 현상 처리