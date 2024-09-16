package com.sideProject.DribbleMatch.controller.recruitment.controller;


import com.sideProject.DribbleMatch.service.recruitment.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/create/{teamId}")
    public String createRecruitmentPage(Model model, @PathVariable Long teamId) {
        model.addAttribute("teamId", teamId);
        return "/team/createRecruitment";
    }
}
