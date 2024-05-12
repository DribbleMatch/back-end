package com.sideProject.DribbleMatch.entity.userTeam;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTeamTest {

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
                .leader(leader)
                .region(region)
                .build();
    }

    @Nested
    @DisplayName("createUserTeamTest")
    public class createUserTeamTest {

        @DisplayName("UserTeam을 생성한다")
        @Test
        public void createUserTeam() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1234!A", region);
            User user = initUser("test2@test.com", "test1234!A", region);
            Team team = initTeam("testTeam", leader, region);

            // when
            TeamMember userTeam = TeamMember.builder()
                    .user(user)
                    .team(team)
                    .build();

            // then
            assertThat(userTeam.getUser()).isEqualTo(user);
            assertThat(userTeam.getTeam()).isEqualTo(team);
        }
    }
}
