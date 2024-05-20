package com.sideProject.DribbleMatch.repository.matching;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sideProject.DribbleMatch.entity.recruitment.QRecruitment.recruitment;
import static com.sideProject.DribbleMatch.entity.region.QRegion.region;
import static com.sideProject.DribbleMatch.entity.team.QTeam.team;
import static com.sideProject.DribbleMatch.entity.matching.QTeamMatch.teamMatch;

@Repository
@RequiredArgsConstructor
public class TeamMatchCustomRepositoryImpl implements TeamMatchCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TeamMatch> find(Pageable pageable, String sido) {

        List<TeamMatch> content = jpaQueryFactory
                .selectFrom(teamMatch)
                .leftJoin(teamMatch.region, region)
                .where(
                        regionEq(sido)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long count = jpaQueryFactory
                .select(recruitment.count())
                .from(recruitment)
                .leftJoin(recruitment.team, team)
                .leftJoin(team.region, region)
                .where(
                        regionEq(sido)
                )
                .fetchOne();

        return new PageImpl<>(
                content,
                pageable,
                count);
    }

    private BooleanExpression regionEq(String sido){
        if(sido==null) {
            return null;
        }
        return region.siDo.eq(sido);
    }
}
