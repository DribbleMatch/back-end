package com.sideProject.DribbleMatch.domain.userTeam.entity;

import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import com.sideProject.DribbleMatch.domain.userTeam.entity.UserTeam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTeamTest {

    @Nested
    @DisplayName("createUserTeamTest")
    public class createUserTeamTest {

        @DisplayName("UserTeam을 생성한다")
        @Test
        public void createUserTeam() {

            // given
            User user = User.builder()
                    .email("test@test.com")
                    .password("test1234")
                    .nickName("test")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(2001, 1, 1))
                    .position(Position.CENTER)
                    .winning(10)
                    .build();

            Team team = Team.builder()
                    .name("test")
                    .region("서울")
                    .winning(10)
                    .build();

            // when
            UserTeam userTeam = UserTeam.builder()
                    .user(user)
                    .team(team)
                    .build();

            // then
            assertThat(userTeam.getUser()).isEqualTo(user);
            assertThat(userTeam.getTeam()).isEqualTo(team);
        }
    }
}
