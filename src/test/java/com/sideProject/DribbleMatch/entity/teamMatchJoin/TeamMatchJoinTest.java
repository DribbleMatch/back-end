package com.sideProject.DribbleMatch.entity.teamMatchJoin;

import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.matching.TeamMatching;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamMatchJoinTest {

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

    public TeamMatching initTeamMatching(Region region) {
        return TeamMatching.builder()
                .name("test")
                .playPeople(5)
                .maxPeople(7)
                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                .status(MatchingStatus.RECRUITING)
                .region(region)
                .build();
    }

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("TeamMatchJoin을 생성한다")
        @Test
        public void createTeamMatchJoin() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);
            TeamMatching teamMatching = initTeamMatching(region);

            // when
            TeamMatchJoin teamMatchJoin = TeamMatchJoin.builder()
                    .team(team)
                    .teamMatching(teamMatching)
                    .build();

            // then
            assertThat(teamMatchJoin.getTeam()).isEqualTo(team);
            assertThat(teamMatchJoin.getTeamMatching()).isEqualTo(teamMatching);
        }
    }
}
