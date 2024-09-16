package com.sideProject.DribbleMatch.controller.recruitment.controller;


import com.sideProject.DribbleMatch.service.recruitment.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitment/page")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    public String recruitmentPage(Model model) {
        model.addAttribute("recruitmentList", recruitmentService.findAllRecruitmentInTime());
        return "/team/recruitment";
    }

    @PostMapping
    public String recruitmentPage(Model model, @RequestParam(name = "searchWord") String searchWord) {
        model.addAttribute("recruitmentList", recruitmentService.findAllRecruitmentInTimeBySearch(searchWord));
        return "/team/recruitment :: #recruitment-list";
    }

    @GetMapping("/create/{teamId}")
    public String createRecruitmentPage(Model model, @PathVariable Long teamId) {
        model.addAttribute("teamId", teamId);
        return "/team/createRecruitment";
    }
}
