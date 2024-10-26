package com.sideProject.DribbleMatch.repository.matching;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.matching.QMatching.matching;
import static com.sideProject.DribbleMatch.entity.personalMatchJoin.QPersonalMatchJoin.personalMatchJoin;
import static com.sideProject.DribbleMatch.entity.team.QTeam.team;
import static com.sideProject.DribbleMatch.entity.teamMatchJoin.QTeamMatchJoin.teamMatchJoin;


@Repository
@RequiredArgsConstructor
public class MatchingCustomRepositoryImpl implements MatchingCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Matching> searchMatchingListByStartDateOrderByStartTime(String searchWord, Pageable pageable, LocalDate date) {
        List<Matching> matchingList = jpaQueryFactory
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

        return new PageImpl<>(matchingList, pageable, total);
    }

    @Override
    public Page<Matching> findPersonalMatchingListByUserIdOrderByStartAt(Long userId, Pageable pageable, MatchingStatus status) {

        List<Matching> matchingList = jpaQueryFactory
                .selectFrom(matching)
                .join(personalMatchJoin).on(personalMatchJoin.matching.id.eq(matching.id))
                .where(personalMatchJoin.user.id.eq(userId)
                        .and(matching.status.eq(status))
                        .and(matching.gameKind.eq(GameKind.PERSONAL)))
                .orderBy(matching.startAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(personalMatchJoin.count())
                .where(personalMatchJoin.user.id.eq(userId))
                .from(personalMatchJoin)

                .fetchOne();

        total = (total == null) ? 0L : total;

        return new PageImpl<>(matchingList, pageable, total);
    }

    @Override
    public Page<Matching> findTeamMatchingListByUserIdOrderByStartAt(Long userId, Pageable pageable, MatchingStatus status) {

        List<Matching> matchingList = jpaQueryFactory
                .selectFrom(matching)
                .join(teamMatchJoin).on(teamMatchJoin.matching.id.eq(matching.id))
                .where(teamMatchJoin.teamMember.user.id.eq(userId)
                        .and(status == MatchingStatus.RECRUITING ?
                                matching.status.eq(status).or(matching.status.eq(MatchingStatus.WAITING_START)) : matching.status.eq(status))
                        .and(matching.gameKind.eq(GameKind.TEAM)))
                .orderBy(matching.startAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(teamMatchJoin.count())
                .where(teamMatchJoin.teamMember.user.id.eq(userId))
                .from(teamMatchJoin)
                .fetchOne();

        total = (total == null) ? 0L : total;

        return new PageImpl<>(matchingList, pageable, total);
    }
}
