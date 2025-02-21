package com.sideProject.ClutchShot.repository.teamMatchJoin;

import com.sideProject.ClutchShot.entity.teamMatchJoin.TeamMatchJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchJoinRepository extends JpaRepository<TeamMatchJoin, Long>, TeamMatchJoinCustomRepository {
}
