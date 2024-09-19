package com.sideProject.DribbleMatch.controller.recruitment.controller;


import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.service.recruitment.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitment/page")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    public String recruitmentPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<RecruitmentResponseDto> recruitmentList = recruitmentService.findAllRecruitmentInTimeBySearch("", pageable);
        model.addAttribute("recruitmentList", recruitmentList);
        model.addAttribute("currentPage", recruitmentList.getPageable().getPageNumber());
        model.addAttribute("totalPage", recruitmentList.getTotalPages());
        return "/team/recruitment";
    }

    @PostMapping
    public String recruitmentPage(Model model,
                                  @RequestParam(name = "searchWord") String searchWord,
                                  @PageableDefault(size = 10) Pageable pageable) {
        Page<RecruitmentResponseDto> recruitmentList = recruitmentService.findAllRecruitmentInTimeBySearch(searchWord, pageable);
        model.addAttribute("recruitmentList", recruitmentList);
        model.addAttribute("currentPage", recruitmentList.getPageable().getPageNumber());
        model.addAttribute("totalPage", recruitmentList.getTotalPages());
        return "/team/recruitment :: #recruitment-list";
    }

    @GetMapping("/create/{teamId}")
    public String createRecruitmentPage(Model model, @PathVariable Long teamId) {
        model.addAttribute("teamId", teamId);
        return "/team/createRecruitment";
    }
}
