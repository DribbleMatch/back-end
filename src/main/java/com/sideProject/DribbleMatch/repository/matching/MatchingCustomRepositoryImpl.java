package com.sideProject.DribbleMatch.repository.matching;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.matching.QMatching.matching;
import static com.sideProject.DribbleMatch.entity.team.QTeam.team;


@Repository
@RequiredArgsConstructor
public class MatchingCustomRepositoryImpl implements MatchingCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Matching> searchMatchingsByStartDateOrderByStartTime(String searchWord, Pageable pageable, LocalDate date) {
        List<Matching> matchings = jpaQueryFactory
                .selectFrom(matching)
                .where(matching.startAt.between(date.atTime(0, 0, 0), date.atTime(23, 59, 59)))
                .where(matching.name.contains(searchWord)
                        .or(matching.region.siDo.contains(searchWord))
                        .or(matching.region.siGunGu.contains(searchWord))
                        .or(matching.region.eupMyeonDongGu.contains(searchWord))
                        .or(matching.region.eupMyeonLeeDong.contains(searchWord))
                        .or(matching.region.lee.contains(searchWord)))
                .orderBy(matching.startAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(matching.count())
                .from(matching)
                .where(matching.startAt.between(date.atTime(0, 0, 0), date.atTime(23, 59, 59)))
                .where(matching.name.contains(searchWord)
                        .or(matching.region.siDo.contains(searchWord))
                        .or(matching.region.siGunGu.contains(searchWord))
                        .or(matching.region.eupMyeonDongGu.contains(searchWord))
                        .or(matching.region.eupMyeonLeeDong.contains(searchWord))
                        .or(matching.region.lee.contains(searchWord)))
                .fetchOne();

        total = (total == null) ? 0L : total;

        return new PageImpl<>(matchings, pageable, total);
    }
}
