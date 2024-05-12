package com.sideProject.DribbleMatch.repository.matching.personalMatching;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
import com.sideProject.DribbleMatch.entity.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sideProject.DribbleMatch.entity.matching.QPersonalMatching.personalMatching;
import static com.sideProject.DribbleMatch.entity.team.QTeam.team;

@Repository
@RequiredArgsConstructor
public class PersonalMatchingCustomRepositoryImpl implements PersonalMatchingCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PersonalMatching> findAll(Pageable pageable) {
        List<PersonalMatching> content = jpaQueryFactory
                .selectFrom(personalMatching)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(personalMatching.count())
                .from(personalMatching)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<PersonalMatching> findByRegionIds(Pageable pageable, List<Long> regionIds) {
        List<PersonalMatching> content = jpaQueryFactory
                .selectFrom(personalMatching)
                .where(personalMatching.region.id.in(regionIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(personalMatching.count())
                .from(personalMatching)
                .where(personalMatching.region.id.in(regionIds))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}
