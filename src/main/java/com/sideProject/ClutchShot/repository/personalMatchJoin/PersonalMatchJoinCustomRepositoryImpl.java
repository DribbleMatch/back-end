package com.sideProject.ClutchShot.repository.personalMatchJoin;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.ClutchShot.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.ClutchShot.entity.personalMatchJoin.PersonalMatchJoin;
import com.sideProject.ClutchShot.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sideProject.ClutchShot.entity.personalMatchJoin.QPersonalMatchJoin.personalMatchJoin;


@Repository
@RequiredArgsConstructor
public class PersonalMatchJoinCustomRepositoryImpl implements PersonalMatchJoinCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long countPersonalMatchJoinByMatchingAndTeam(Long matchingId, PersonalMatchingTeam matchingTeam) {
        return jpaQueryFactory.select(personalMatchJoin.count())
                .from(personalMatchJoin)
                .where(personalMatchJoin.matching.id.eq(matchingId)
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
