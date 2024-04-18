package com.sideProject.DribbleMatch.domain.team.repository;

import com.sideProject.DribbleMatch.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
