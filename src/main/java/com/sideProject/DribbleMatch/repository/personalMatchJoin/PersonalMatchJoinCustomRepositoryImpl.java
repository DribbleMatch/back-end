package com.sideProject.DribbleMatch.repository.personalMatchJoin;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.PersonalMatchJoin;
import com.sideProject.DribbleMatch.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.sideProject.DribbleMatch.entity.personalMatchJoin.QPersonalMatchJoin.personalMatchJoin;


@Repository
@RequiredArgsConstructor
public class PersonalMatchJoinCustomRepositoryImpl implements PersonalMatchJoinCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long countPersonalMatchJoinByMatchingAndTeam(Matching matching, PersonalMatchingTeam matchingTeam) {
        return jpaQueryFactory.select(personalMatchJoin.count())
                .from(personalMatchJoin)
                .where(personalMatchJoin.matching.eq(matching)
                        .and(personalMatchJoin.matchingTeam.eq(matchingTeam)))
                .fetchOne();
    }

    @Override
    public List<User> findUserByMatchingAndTeam(Long matchingId, PersonalMatchingTeam personalMatchingTeam) {
        return jpaQueryFactory
                .select(personalMatchJoin.user)
                .from(personalMatchJoin)
                .where(personalMatchJoin.matching.id.eq(matchingId)
                        .and(personalMatchJoin.matchingTeam.eq(personalMatchingTeam)))
                .fetch();
    }

    @Override
    public Optional<PersonalMatchJoin> findByMatchingIdAndUserId(Long matchingId, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(personalMatchJoin)
                .where(personalMatchJoin.matching.id.eq(matchingId)
                        .and(personalMatchJoin.user.id.eq(userId)))
                .fetchOne());
    }
}
