package com.sideProject.DribbleMatch.domain.teamMatchJoin.entity;

import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingType;
import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamMatchJoinTest {

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("TeamMatchJoin을 생성한다")
        @Test
        public void createTeamMatchJoin() {

            // given
            Team team = Team.builder()
                    .name("test")
                    .region("서울")
                    .winning(10)
                    .build();

            Matching matching = Matching.builder()
                    .name("test")
                    .people(10)
                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                    .place("서울")
                    .status(MatchingStatus.RECRUITING)
                    .type(MatchingType.TEAM)
                    .build();

            // when
            TeamMatchJoin teamMatchJoin = TeamMatchJoin.builder()
                    .team(team)
                    .matching(matching)
                    .build();

            // then
            assertThat(teamMatchJoin.getTeam()).isEqualTo(team);
            assertThat(teamMatchJoin.getMatching()).isEqualTo(matching);
        }
    }
}
