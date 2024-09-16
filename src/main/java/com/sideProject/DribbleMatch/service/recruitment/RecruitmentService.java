package com.sideProject.DribbleMatch.service.recruitment;

import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;

import java.util.List;

public interface RecruitmentService {

    public List<RecruitmentResponseDto> findAllRecruitmentInTime();
    public List<RecruitmentResponseDto> findAllRecruitmentInTimeBySearch(String searchWord);
    public void createRecruitment(RecruitmentCreateRequestDto requestDto);
}
