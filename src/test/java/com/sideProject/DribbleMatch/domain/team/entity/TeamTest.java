package com.sideProject.DribbleMatch.domain.team.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("Team을 생성한다")
        @Test
        public void createTeam() {

            // given, when
            Team team = Team.builder()
                    .name("test")
                    .region("서울")
                    .winning(10)
                    .build();

            // then
            assertThat(team.getName()).isEqualTo("test");
            assertThat(team.getRegion()).isEqualTo("서울");
            assertThat(team.getWinning()).isEqualTo(10);
        }
    }
}
