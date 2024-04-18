package com.sideProject.DribbleMatch.domain.user.entity;

import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("createUser")
        @Test
        public void createUser() {

            // given, when
            User user = User.builder()
                    .email("test@test.com")
                    .password("test1234")
                    .nickName("test")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(2001, 1, 1))
                    .position(Position.CENTER)
                    .winning(10)
                    .build();

            // then
            assertThat(user.getEmail()).isEqualTo("test@test.com");
            assertThat(user.getPassword()).isEqualTo("test1234");
            assertThat(user.getNickName()).isEqualTo("test");
            assertThat(user.getGender()).isEqualTo(Gender.MALE);
            assertThat(user.getBirth()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(user.getPosition()).isEqualTo(Position.CENTER);
            assertThat(user.getWinning()).isEqualTo(10);
        }
    }
}