package com.sideProject.DribbleMatch.entity.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.entity.team.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

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
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("Team을 생성한다")
        @Test
        public void createTeam() {

            // given, when
            Region region = initRegion("당산동");

            User leader = initUser("test@test.com", "test", region);

            Team team = Team.builder()
                    .name("testTeam")
                    .winning(10)
                    .leader(leader)
                    .region(region)
                    .build();

            // then
            assertThat(team.getName()).isEqualTo("testTeam");
            assertThat(team.getWinning()).isEqualTo(10);
            assertThat(team.getLeader()).isEqualTo(leader);
            assertThat(team.getRegion()).isEqualTo(region);
        }
    }

    @Nested
    @DisplayName("UpdateTest")
    public class UpdateTest {

        @DisplayName("Team을 수정한다")
        @Test
        public void updateTeam() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            TeamUpdateRequestDto requestDto = TeamUpdateRequestDto.builder()
                    .name("newTeam")
                    .regionString("서울특별시 영등포구 문래동")
                    .leaderId(2L)
                    .build();
            User newLeader = initUser("test2@test.com", "test2", region);
            Region newRegion = initRegion("문래동");

            // when
            team.updateTeam(requestDto, newLeader, newRegion);

            // then
            assertThat(team.getName()).isEqualTo("newTeam");
            assertThat(team.getLeader()).isEqualTo(newLeader);
            assertThat(team.getRegion()).isEqualTo(newRegion);
        }
    }
}
