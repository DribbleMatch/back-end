package com.sideProject.DribbleMatch.repository.teamMatchJoin;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.repository.matching.MatchingCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

import static com.sideProject.DribbleMatch.entity.matching.QMatching.matching;
import static com.sideProject.DribbleMatch.entity.team.QTeam.team;
import static com.sideProject.DribbleMatch.entity.teamMatchJoin.QTeamMatchJoin.teamMatchJoin;
import static com.sideProject.DribbleMatch.entity.teamMember.QTeamMember.teamMember;


@Repository
@RequiredArgsConstructor
public class TeamMatchJoinCustomRepositoryImpl implements TeamMatchJoinCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int countTeamMatchJoinByMatchingAndGroupByTeam(Matching matching, int teamNum) {
        List<Tuple> resultList = jpaQueryFactory
                .select(teamMatchJoin.teamMember.team.id, teamMatchJoin.count())
                .from(teamMatchJoin)
                .where(teamMatchJoin.matching.eq(matching))
                .groupBy(teamMatchJoin.teamMember.team.id)
                .fetch();

        if (resultList.size() == 2) {
            return Objects.requireNonNull(resultList.get(teamNum).get(teamMatchJoin.count())).intValue();
        } else if (resultList.size() == 1) {
            return teamNum == 0 ? Objects.requireNonNull(resultList.get(teamNum).get(teamMatchJoin.count())).intValue() : 0;
        } else {
            throw new CustomException(ErrorCode.INVALID_TEAM_MATCH_INFO);
        }

    }

    @Override
    public LinkedHashMap<String, List<TeamMember>> findTeamInfoByMatchingId(Long matchingId) {

        List<Tuple> result = jpaQueryFactory
                .select(team.name, teamMatchJoin.teamMember)
                .from(teamMatchJoin)
                .join(teamMatchJoin.teamMember, teamMember)
                .join(teamMember.team, team)
                .where(teamMatchJoin.matching.id.eq(matchingId))
                .fetch();

        LinkedHashMap<String, List<TeamMember>> teamMembersByTeam = new LinkedHashMap<>();

        for (Tuple tuple : result) {
            String teamName = tuple.get(team.name);
            TeamMember teamMember = tuple.get(teamMatchJoin.teamMember);

            teamMembersByTeam
                    .computeIfAbsent(teamName, k -> new ArrayList<>())
                    .add(teamMember);
        }

        return teamMembersByTeam;
    }

    @Override
    public Optional<TeamMatchJoin> findByMatchingIdAndUserId(Long matchingId, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(teamMatchJoin)
                .where(teamMatchJoin.matching.id.eq(matchingId)
                        .and(teamMatchJoin.teamMember.user.id.eq(userId)))
                .fetchOne());
    }

    @Override
    public List<String> findTeamNameListByMatching(Matching matching) {
        return jpaQueryFactory
                .select(teamMatchJoin.teamMember.team.name)
                .from(teamMatchJoin)
                .where(teamMatchJoin.matching.eq(matching))
                .groupBy(teamMatchJoin.teamMember.team.name)
                .fetch();
    }
}
