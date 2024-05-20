package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TeamMatchCustomRepository {
    public Page<TeamMatch> find(Pageable pageable, String sido, LocalDateTime now);
}
