package com.sideProject.DribbleMatch.controller.recruitment.restController;

import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.service.recruitment.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruitment/rest")
public class RecruitmentRestController {

    private final RecruitmentService recruitmentService;

    @PostMapping("/create")
    public void createRecruitment(@RequestBody RecruitmentCreateRequestDto requestDto) {
        recruitmentService.createRecruitment(requestDto);
    }
}
