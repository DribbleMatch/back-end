package com.sideProject.DribbleMatch.repository.teamApplication;

import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {
    public List<TeamApplication> findTeamApplicationByTeamIdAndStatus(Long teamId, ApplicationStatus status);
    public List<TeamApplication> findTeamApplicationByUserId(Long userId);
    public Optional<TeamApplication> findTeamApplicationByUserIdAndTeamIdAndStatus(Long userId, Long teamId, ApplicationStatus status);
}
