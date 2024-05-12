package com.sideProject.DribbleMatch.service.recruitment;

import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface RecruitmentService {
    public Long create(RecruitmentCreateRequestDto request, Long teamId, Long userId);
    public Long update(RecruitmentUpdateRequestDto request, Long userId);
    public Page<RecruitmentResponseDto> find();
    public Long delete(Long recruitmentId, Long userId);
}
