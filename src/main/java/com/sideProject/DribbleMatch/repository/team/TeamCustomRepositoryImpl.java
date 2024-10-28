package com.sideProject.DribbleMatch.repository.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.entity.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sideProject.DribbleMatch.entity.team.QTeam.team;

@Repository
@RequiredArgsConstructor
public class TeamCustomRepositoryImpl implements TeamCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Team> findBySearch(String searchWord, Pageable pageable) {

        List<Team> teams = jpaQueryFactory
                .selectFrom(team)
                .where(team.name.contains(searchWord)
                        .or(team.region.siDo.contains(searchWord))
                        .or(team.region.siGunGu.contains(searchWord))
                        .or(team.region.eupMyeonDongGu.contains(searchWord))
                        .or(team.region.eupMyeonLeeDong.contains(searchWord))
                        .or(team.region.lee.contains(searchWord)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(team.count())
                .from(team)
                .where(team.name.contains(searchWord)
                        .or(team.region.siDo.contains(searchWord))
                        .or(team.region.siGunGu.contains(searchWord))
                        .or(team.region.eupMyeonDongGu.contains(searchWord))
                        .or(team.region.eupMyeonLeeDong.contains(searchWord))
                        .or(team.region.lee.contains(searchWord)))
                .fetchOne();

        total = (total == null) ? 0L : total;

        return new PageImpl<>(teams, pageable, total);
    }
}
