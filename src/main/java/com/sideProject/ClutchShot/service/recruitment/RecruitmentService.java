package com.sideProject.ClutchShot.service.recruitment;

import com.sideProject.ClutchShot.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.ClutchShot.dto.recruitment.response.RecruitmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentService {

    public Long createRecruitment(RecruitmentCreateRequestDto requestDto);
    public Page<RecruitmentResponseDto> searchRecruitments(String searchWord, Pageable pageable);
}
