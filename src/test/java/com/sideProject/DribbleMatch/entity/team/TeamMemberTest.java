package com.sideProject.DribbleMatch.entity.team;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamMemberTest {

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
    @DisplayName("CreateTeamMemberTest")
    public class CreateTeamMemberTest {

        @DisplayName("TeamMember을 생성한다")
        @Test
        public void createTeamMember() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1234!A", region);
            User user = initUser("test2@test.com", "test1234!A", region);
            Team team = initTeam("testTeam", leader, region);

            // when
            TeamMember teamMember = TeamMember.builder()
                    .user(user)
                    .team(team)
                    .teamRole(TeamRole.MEMBER)
                    .build();

            // then
            assertThat(teamMember.getUser()).isEqualTo(user);
            assertThat(teamMember.getTeam()).isEqualTo(team);
            assertThat(teamMember.getTeamRole()).isEqualTo(TeamRole.MEMBER);
        }
    }

    @Nested
    @DisplayName("ChangeTeamRoleTest")
    public class ChangeTeamRoleTest {

        @DisplayName("TeamMember의 권한을 ADMIN으로 변경한다")
        @Test
        public void changeTeamRole() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1234!A", region);
            User user = initUser("test2@test.com", "test1234!A", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember teamMember = TeamMember.builder()
                    .user(user)
                    .team(team)
                    .teamRole(TeamRole.MEMBER)
                    .build();

            // when
            teamMember.changeTeamRole(TeamRole.ADMIN);

            // then
            assertThat(teamMember.getTeamRole()).isEqualTo(TeamRole.ADMIN);
        }

        @DisplayName("TeamMember의 권한을 MEMBER로 변경한다")
        @Test
        public void changeTeamRole2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1234!A", region);
            User user = initUser("test2@test.com", "test1234!A", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember teamMember = TeamMember.builder()
                    .user(user)
                    .team(team)
                    .teamRole(TeamRole.MEMBER)
                    .build();

            // when
            teamMember.changeTeamRole(TeamRole.ADMIN);

            // then
            assertThat(teamMember.getTeamRole()).isEqualTo(TeamRole.ADMIN);
        }
    }
}
