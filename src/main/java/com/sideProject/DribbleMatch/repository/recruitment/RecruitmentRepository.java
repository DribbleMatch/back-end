package com.sideProject.DribbleMatch.repository.recruitment;

import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentCustomRepository {
}
