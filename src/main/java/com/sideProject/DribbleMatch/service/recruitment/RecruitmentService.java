package com.sideProject.DribbleMatch.service.recruitment;

import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitmentService {

    public Long createRecruitment(RecruitmentCreateRequestDto requestDto);
    public Page<RecruitmentResponseDto> searchRecruitments(String searchWord, Pageable pageable);
}
