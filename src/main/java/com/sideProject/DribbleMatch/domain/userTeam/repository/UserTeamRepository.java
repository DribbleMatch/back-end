package com.sideProject.DribbleMatch.domain.userTeam.repository;

import com.sideProject.DribbleMatch.domain.userTeam.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
}
