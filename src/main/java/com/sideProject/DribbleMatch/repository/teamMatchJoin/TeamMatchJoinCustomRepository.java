package com.sideProject.DribbleMatch.repository.teamMatchJoin;

import com.querydsl.core.Tuple;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sideProject.DribbleMatch.entity.personalMatchJoin.QPersonalMatchJoin.personalMatchJoin;

public interface TeamMatchJoinCustomRepository {
    public int countTeamMatchJoinByMatchingAndGroupByTeam(Matching matching, int teamNum);
    public LinkedHashMap<String, List<TeamMember>> findTeamInfoByMatchingId(Long matchingId);
    public Optional<TeamMatchJoin> findByMatchingIdAndUserId(Long matchingId, Long userId);
    public List<String> findTeamNameListByMatching(Matching matching);
}
