package com.sideProject.DribbleMatch.controller.matching.controller;

import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {

    private final RegionRepository regionRepository;

    @GetMapping("/createMatching")
    public String createMatchingPage(Model model) {
        model.addAttribute("siDoList", regionRepository.findAllSiDo());
        return "createMatching";
    }
}
