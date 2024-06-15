package com.sideProject.DribbleMatch.entity.user;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminTest {

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

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("createAdmin")
        @Test
        public void createAdmin() {

            // given, when
            Admin admin = Admin.builder()
                    .email("test@test.com")
                    .password("test1234!A")
                    .nickName("test")
                    .build();

            // then
            assertThat(admin.getEmail()).isEqualTo("test@test.com");
            assertThat(admin.getPassword()).isEqualTo("test1234!A");
            assertThat(admin.getNickName()).isEqualTo("test");
        }
    }

    @Nested
    @DisplayName("UserToAdminTest")
    public class UserToAdminTest {

        @DisplayName("회원을 관리자로 변환한다")
        @Test
        public void userToAdmin() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test@test.com", "test", region);

            // when
            Admin changedAdmin = Admin.userToAdmin(user);

            // then
            assertThat(changedAdmin.getEmail()).isEqualTo(user.getEmail());
            assertThat(changedAdmin.getPassword()).isEqualTo(user.getPassword());
            assertThat(changedAdmin.getNickName()).isEqualTo(user.getNickName());
        }
    }
}
