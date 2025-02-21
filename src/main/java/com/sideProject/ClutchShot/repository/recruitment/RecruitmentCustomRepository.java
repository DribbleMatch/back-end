package com.sideProject.ClutchShot.repository.recruitment;

import com.sideProject.ClutchShot.entity.recruitment.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentCustomRepository {
    public Page<Recruitment> searchRecruitmentsInTimeOrderByCreatedAt(String searchWord, Pageable pageable);
}
