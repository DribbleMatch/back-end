package com.sideProject.DribbleMatch.domain.recruitment.repository;

import com.sideProject.DribbleMatch.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}
