package com.sideProject.DribbleMatch.entity.recruitment;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class RecruitmentTest {

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

        @DisplayName("Recruitment를 생성한다")
        @Test
        public void createRecruitment() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

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
