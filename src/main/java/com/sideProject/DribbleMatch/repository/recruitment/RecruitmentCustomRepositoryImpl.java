package com.sideProject.DribbleMatch.repository.recruitment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.recruitment.QRecruitment.recruitment;
import static com.sideProject.DribbleMatch.entity.team.QTeam.team;


@RequiredArgsConstructor
@Repository
public class RecruitmentCustomRepositoryImpl implements RecruitmentCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Recruitment> searchRecruitmentsInTimeOrderByCreatedAt(String searchWord, Pageable pageable) {

        List<Recruitment> recruitments =  jpaQueryFactory
                .selectFrom(recruitment)
                .where(recruitment.endAt.goe(LocalDate.now()))
                .where(recruitment.team.name.contains(searchWord)
                        .or(recruitment.title.contains(searchWord))
                        .or(recruitment.positionString.contains(searchWord)))
                .orderBy(recruitment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(recruitment.count())
                .from(recruitment)
                .where(recruitment.endAt.goe(LocalDate.now()))
                .where(recruitment.team.name.contains(searchWord)
                        .or(recruitment.title.contains(searchWord))
                        .or(recruitment.positionString.contains(searchWord)))
                .fetchOne();

        total = (total == null) ? 0L : total;

        return new PageImpl<>(recruitments, pageable, total);
    }
}
