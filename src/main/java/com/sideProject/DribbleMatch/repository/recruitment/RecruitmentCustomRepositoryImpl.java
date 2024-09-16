package com.sideProject.DribbleMatch.repository.recruitment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.recruitment.QRecruitment.recruitment;


@RequiredArgsConstructor
@Repository
public class RecruitmentCustomRepositoryImpl implements RecruitmentCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Recruitment> findRecruitmentInTimeOrderByCreateAt() {
        return jpaQueryFactory
                .selectFrom(recruitment)
                .where(recruitment.endAt.goe(LocalDate.now()))
                .orderBy(recruitment.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Recruitment> findRecruitmentInTimeOrderByCreateAtBySearch(String searchWord) {
        return jpaQueryFactory
                .selectFrom(recruitment)
                .where(recruitment.endAt.goe(LocalDate.now()))
                .where(recruitment.team.name.contains(searchWord)
                        .or(recruitment.title.contains(searchWord))
                        .or(recruitment.positionString.contains(searchWord)))
                .orderBy(recruitment.createdAt.desc())
                .fetch();
    }
}
