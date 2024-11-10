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
import com.sideProject.DribbleMatch.entity.user.User;
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
    public Long countTeamMatchJoinByMatchingIdAndTeamName(Long matchingId, String teamName) {
        return jpaQueryFactory
                .select(teamMatchJoin.count())
                .from(teamMatchJoin)
                .where(teamMatchJoin.matching.id.eq(matchingId)
                        .and(teamMatchJoin.teamMember.team.name.eq(teamName)))
                .fetchOne();
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
    public List<User> findAllUsersByMatchingIdAndTeamName(Long matchingId, String teamName) {
        return jpaQueryFactory
                .select(teamMatchJoin.teamMember.user)
                .from(teamMatchJoin)
                .where(teamMatchJoin.teamMember.team.name.eq(teamName)
                        .and(teamMatchJoin.matching.id.eq(matchingId)))
                .fetch();
    }
}
