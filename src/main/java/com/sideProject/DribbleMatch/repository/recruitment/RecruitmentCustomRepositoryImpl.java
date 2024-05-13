package com.sideProject.DribbleMatch.repository.recruitment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentSearchParamRequest;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.sideProject.DribbleMatch.entity.recruitment.QRecruitment.recruitment;
import static com.sideProject.DribbleMatch.entity.team.QTeam.team;
import static com.sideProject.DribbleMatch.entity.region.QRegion.region;

@RequiredArgsConstructor
public class RecruitmentCustomRepositoryImpl implements RecruitmentCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Recruitment> find(Pageable pageable, RecruitmentSearchParamRequest param) {
        List<Recruitment> content = jpaQueryFactory
                .selectFrom(recruitment)
                .leftJoin(recruitment.team, team)
                .leftJoin(team.region, region)
                .where(
                        regionEq(param.getRegion())
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
                        regionEq(param.getRegion())
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
