package com.sideProject.ClutchShot.repository.recruitment;

import com.sideProject.ClutchShot.entity.recruitment.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentCustomRepository {
}
