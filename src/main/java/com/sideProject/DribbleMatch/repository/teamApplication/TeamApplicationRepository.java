package com.sideProject.DribbleMatch.repository.teamApplication;

import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {
    public Page<TeamApplication> findAll(Pageable pageable);
}
