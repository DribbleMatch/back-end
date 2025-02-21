package com.sideProject.ClutchShot.repository.teamApplication;

import com.sideProject.ClutchShot.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.ClutchShot.entity.teamApplication.TeamApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {
    public List<TeamApplication> findTeamApplicationByTeamIdAndStatus(Long teamId, ApplicationStatus status);
    public Optional<TeamApplication> findTeamApplicationByUserIdAndTeamIdAndStatus(Long userId, Long teamId, ApplicationStatus status);
}
