package com.sideProject.DribbleMatch.domain.teamMatchJoin.repository;

import com.sideProject.DribbleMatch.domain.teamMatchJoin.entity.TeamMatchJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchJoinRepository extends JpaRepository<TeamMatchJoin, Long> {
}
