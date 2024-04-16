package com.sideProject.DribbleMatch.domain.recruitment.entity;

import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecruitmentTest {

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("Recruitment를 생성한다")
        @Test
        public void createRecruitment() {

            // given
            Team team = Team.builder()
                    .name("test")
                    .region("서울")
                    .winning(10)
                    .build();

            // when
            Recruitment recruitment = Recruitment.builder()
                    .title("test")
                    .content("test recruitment")
                    .position(Position.CENTER)
                    .winning(10)
                    .team(team)
                    .build();

            // then
            assertThat(recruitment.getTitle()).isEqualTo("test");
            assertThat(recruitment.getContent()).isEqualTo("test recruitment");
            assertThat(recruitment.getPosition()).isEqualTo(Position.CENTER);
            assertThat(recruitment.getWinning()).isEqualTo(10);
            assertThat(recruitment.getTeam()).isEqualTo(team);
        }
    }
}
