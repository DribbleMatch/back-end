package com.sideProject.DribbleMatch.entity.team;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.JoinStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamApplicationTest {

    private Region initRegion(String dong) {
        return Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build();
    }

    private User initUser(String email, String name, Region region) {
        return User.builder()
                .email(email)
                .password("test1234!A")
                .nickName(name)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .region(region)
                .build();
    }

    private Team initTeam(String name, User leader, Region region) {
        return Team.builder()
                .name(name)
                .winning(10)
                .maxNumber(10)
                .info("testInfo")
                .leader(leader)
                .region(region)
                .build();
    }

    @Nested
    @DisplayName("CreateTeamApplicationTest")
    public class CreateTeamApplicationTest {

        @DisplayName("Application을 생성한다")
        @Test
        public void createTeamApplication() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1234!A", region);
            User user = initUser("test2@test.com", "test1234!A", region);
            Team team = initTeam("testTeam", leader, region);

            // when
            TeamApplication teamApplication = TeamApplication.builder()
                    .user(user)
                    .team(team)
                    .introduce("testIntroduce")
                    .build();

            // then
            assertThat(teamApplication.getUser()).isEqualTo(user);
            assertThat(teamApplication.getTeam()).isEqualTo(team);
            assertThat(teamApplication.getIntroduce()).isEqualTo("testIntroduce");
            assertThat(teamApplication.getStatus()).isEqualTo(JoinStatus.WAIT);
        }
    }

    @Nested
    @DisplayName("UpdateTeamApplicationTest")
    public class UpdateTeamApplicationTest {

        @DisplayName("Application을 승인한다")
        @Test
        public void updateTeamApplication() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1234!A", region);
            User user = initUser("test2@test.com", "test1234!A", region);
            Team team = initTeam("testTeam", leader, region);

            TeamApplication teamApplication = TeamApplication.builder()
                    .user(user)
                    .team(team)
                    .introduce("testIntroduce")
                    .build();

            // when
            teamApplication.approve();

            // then
            assertThat(teamApplication.getUser()).isEqualTo(user);
            assertThat(teamApplication.getTeam()).isEqualTo(team);
            assertThat(teamApplication.getIntroduce()).isEqualTo("testIntroduce");
            assertThat(teamApplication.getStatus()).isEqualTo(JoinStatus.APPROVE);
        }

        @DisplayName("Application을 거절한다")
        @Test
        public void updateTeamApplication2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1234!A", region);
            User user = initUser("test2@test.com", "test1234!A", region);
            Team team = initTeam("testTeam", leader, region);

            TeamApplication teamApplication = TeamApplication.builder()
                    .user(user)
                    .team(team)
                    .introduce("testIntroduce")
                    .build();

            // when
            teamApplication.refuse();

            // then
            assertThat(teamApplication.getUser()).isEqualTo(user);
            assertThat(teamApplication.getTeam()).isEqualTo(team);
            assertThat(teamApplication.getIntroduce()).isEqualTo("testIntroduce");
            assertThat(teamApplication.getStatus()).isEqualTo(JoinStatus.REFUSE);
        }
    }
}