package com.sideProject.DribbleMatch.service.recruitment;

import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentSearchParamRequest;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentService {
    public Long create(RecruitmentCreateRequestDto request, Long userId);
    public Long update(RecruitmentUpdateRequestDto request, Long id, Long userId);
    public Page<RecruitmentResponseDto> find(Pageable pageable, RecruitmentSearchParamRequest param);
    public RecruitmentResponseDto findById(Long id);
    public Long delete(Long recruitmentId, Long userId);
}
