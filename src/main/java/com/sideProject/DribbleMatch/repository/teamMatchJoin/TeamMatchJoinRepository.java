package com.sideProject.DribbleMatch.repository.teamMatchJoin;

import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchJoinRepository extends JpaRepository<TeamMatchJoin, Long> {
    int countTeamMatchJoinByTeam(Team team);
}
