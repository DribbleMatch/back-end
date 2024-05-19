package com.sideProject.DribbleMatch.repository.teamMatchJoin;

import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMatchJoinRepository extends JpaRepository<TeamMatchJoin, Long> {

    List<TeamMatchJoin> findByTeamMatch(TeamMatch teamMatch);
}
