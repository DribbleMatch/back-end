package com.sideProject.DribbleMatch.controller.team;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.service.recruitment.RecruitmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/recruitment")
public class TeamRecruitmentController {
    RecruitmentService recruitmentService;
    @GetMapping("/{id}")
    public ApiResponse<RecruitmentResponseDto> find(@PathVariable("id") Long id) {

        return ApiResponse.ok(recruitmentService.findById(id));
    }

    @PostMapping
    public ApiResponse<Long> createRecruitment(
            Principal principal,
            @RequestBody @Valid RecruitmentCreateRequestDto request) {
        return ApiResponse.ok(recruitmentService.create(request,Long.valueOf(principal.getName())));
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> updateTeam(
            Principal principal,
            @PathVariable("id") Long id,
            @RequestBody @Valid RecruitmentUpdateRequestDto request) {
        return ApiResponse.ok(recruitmentService.update(request,id, Long.valueOf(principal.getName())));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Long> deleteTeam(Principal principal,@PathVariable("id") Long id) {
        return ApiResponse.ok(recruitmentService.delete(id,Long.valueOf(principal.getName())));
    }
}
