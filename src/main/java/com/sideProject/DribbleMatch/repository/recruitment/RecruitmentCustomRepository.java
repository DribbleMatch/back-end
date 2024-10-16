package com.sideProject.DribbleMatch.repository.recruitment;

import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentSearchParamRequest;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitmentCustomRepository {
    public Page<Recruitment> searchRecruitmentsInTimeOrderByCreatedAt(String searchWord, Pageable pageable);
}
