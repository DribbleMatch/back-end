package com.sideProject.DribbleMatch.entity.user;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private Region initRegion(String dong) {
        return Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build();
    }

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("createUser")
        @Test
        public void createUser() {

            // given, when
            Region region = initRegion("당산동");
            User user = User.builder()
                    .email("test@test.com")
                    .password("test1234!A")
                    .nickName("test")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(2001, 1, 1))
                    .position(Position.CENTER)
                    .winning(10)
                    .region(region)
                    .build();

            // then
            assertThat(user.getEmail()).isEqualTo("test@test.com");
            assertThat(user.getPassword()).isEqualTo("test1234!A");
            assertThat(user.getNickName()).isEqualTo("test");
            assertThat(user.getGender()).isEqualTo(Gender.MALE);
            assertThat(user.getBirth()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(user.getPosition()).isEqualTo(Position.CENTER);
            assertThat(user.getWinning()).isEqualTo(10);
            assertThat(user.getRegion()).isEqualTo(region);
        }
    }
}