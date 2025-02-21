package com.sideProject.ClutchShot.repository.teamMember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.ClutchShot.entity.teamMember.ENUM.TeamRole;
import com.sideProject.ClutchShot.entity.teamMember.TeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sideProject.ClutchShot.entity.teamMember.QTeamMember.teamMember;

@Repository
@RequiredArgsConstructor
public class TeamMemberCustomRepositoryImpl implements TeamMemberCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<String> findTeamNameByUserId(Long userId) {
        return jpaQueryFactory
                .select(teamMember.team.name)
                .from(teamMember)
                .where(teamMember.user.id.eq(userId)
                        .and(teamMember.teamRole.eq(TeamRole.ADMIN)))
                .fetch();
    }

    @Override
    public List<TeamMember> findAllByTeamName(String teamName) {
        return jpaQueryFactory
                .selectFrom(teamMember)
                .where(teamMember.team.name.eq(teamName))
                .fetch();
    }
}
