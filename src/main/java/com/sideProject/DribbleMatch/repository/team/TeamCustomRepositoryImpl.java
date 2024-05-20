package com.sideProject.DribbleMatch.repository.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.team.QTeam.team;

@Repository
@RequiredArgsConstructor
public class TeamCustomRepositoryImpl implements TeamCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Team> findAll(Pageable pageable) {
        List<Team> content = jpaQueryFactory
                .selectFrom(team)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(team.count())
                .from(team)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<Team> findByRegionIds(Pageable pageable, List<Long> regionIds) {
        List<Team> content = jpaQueryFactory
                .selectFrom(team)
                .where(team.region.id.in(regionIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(team.count())
                .from(team)
                .where(team.region.id.in(regionIds))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}
