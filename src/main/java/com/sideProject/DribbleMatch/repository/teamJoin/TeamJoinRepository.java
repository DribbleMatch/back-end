package com.sideProject.DribbleMatch.repository.teamJoin;

import com.sideProject.DribbleMatch.entity.joinTeam.TeamJoin;
import com.sideProject.DribbleMatch.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJoinRepository extends JpaRepository<TeamJoin, Long> {
}
