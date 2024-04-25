package com.sideProject.DribbleMatch.domain.team.entity;

import com.sideProject.DribbleMatch.domain.team.dto.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    private User initUser(String email, String name) {
        return User.builder()
                .email(email)
                .password("test1234")
                .nickName(name)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .build();
    }

    private Team initTeam(String name, User leader) {
        return Team.builder()
                .name(name)
                .region("서울시 영등포구")
                .winning(10)
                .leader(leader)
                .build();
    }

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("Team을 생성한다")
        @Test
        public void createTeam() {

            // given, when
            User leader = initUser("test@test.com", "test");

            Team team = Team.builder()
                    .name("testTeam")
                    .region("서울시 영등포구")
                    .winning(10)
                    .leader(leader)
                    .build();

            // then
            assertThat(team.getName()).isEqualTo("testTeam");
            assertThat(team.getRegion()).isEqualTo("서울시 영등포구");
            assertThat(team.getWinning()).isEqualTo(10);
            assertThat(team.getLeader()).isEqualTo(leader);
        }
    }

    @Nested
    @DisplayName("UpdateTest")
    public class UpdateTest {

        @DisplayName("Team을 수정한다")
        @Test
        public void updateTeam() {

            // given
            User leader = initUser("test@test.com", "test");
            Team team = initTeam("testTeam", leader);

            User leader2 = initUser("test2@test.com", "test2");
            TeamUpdateRequestDto request = TeamUpdateRequestDto.builder()
                    .name("testTeam2")
                    .region("서울시 중구")
                    .build();

            // when
            team.updateTeam(request, leader2);

            // then
            assertThat(team.getName()).isEqualTo("testTeam2");
            assertThat(team.getRegion()).isEqualTo("서울시 중구");
            assertThat(team.getLeader()).isEqualTo(leader2);
        }
    }
}
