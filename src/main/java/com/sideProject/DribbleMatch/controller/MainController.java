package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MatchingService matchingService;

    @GetMapping("/page")
    public String index(ModelMap model) {

        LocalDate nowDate = LocalDate.now();
        model.addAttribute("nowDate", nowDate);

        model.addAttribute("matchingList", null);

        return "/index";
    }

    @GetMapping("/login/page")
    public String login(ModelMap model) {

        return "/login/login";
    }
}
//todo: 매핑 안된 URL에도 토큰 인증 실패 json이 가는 현상 처리