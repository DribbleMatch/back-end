package com.sideProject.ClutchShot.repository.teamMatchJoin;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.ClutchShot.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.ClutchShot.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.sideProject.ClutchShot.entity.teamMatchJoin.QTeamMatchJoin.teamMatchJoin;


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
