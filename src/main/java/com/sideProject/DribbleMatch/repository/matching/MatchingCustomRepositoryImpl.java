package com.sideProject.DribbleMatch.repository.matching;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.matching.QMatching.matching;


@Repository
@RequiredArgsConstructor
public class MatchingCustomRepositoryImpl implements MatchingCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Matching> findByStartDateOrderByStartTime(LocalDate localDate) {

        return jpaQueryFactory
                .selectFrom(matching)
                .where(matching.startAt.between(localDate.atTime(0, 0, 0), localDate.atTime(23, 59, 59)))
                .orderBy(matching.startAt.desc())
                .fetch();
    }
}
