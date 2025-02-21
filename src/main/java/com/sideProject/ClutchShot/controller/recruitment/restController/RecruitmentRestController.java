package com.sideProject.ClutchShot.controller.recruitment.restController;

import com.sideProject.ClutchShot.common.response.ApiResponse;
import com.sideProject.ClutchShot.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.ClutchShot.service.recruitment.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruitment")
public class RecruitmentRestController {

    private final RecruitmentService recruitmentService;

    @PostMapping("/create")
    public ApiResponse<Long> createRecruitment(@RequestBody RecruitmentCreateRequestDto requestDto) {
        return ApiResponse.ok(recruitmentService.createRecruitment(requestDto));
    }
}
