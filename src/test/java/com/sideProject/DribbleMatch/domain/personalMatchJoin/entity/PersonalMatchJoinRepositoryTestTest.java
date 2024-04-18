package com.sideProject.DribbleMatch.domain.personalMatchJoin.entity;

import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingType;
import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class PersonalMatchJoinRepositoryTestTest {

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("PersonalMatchJoin을 생성한다")
        @Test
        public void createPersonalMatchJoin() {

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

            Matching matching = Matching.builder()
                    .name("test")
                    .people(10)
                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                    .place("서울")
                    .status(MatchingStatus.RECRUITING)
                    .type(MatchingType.TEAM)
                    .build();

            // when
            PersonalMatchJoin personalMatchJoin = PersonalMatchJoin.builder()
                    .user(user)
                    .matching(matching)
                    .build();

            // then
            assertThat(personalMatchJoin.getUser()).isEqualTo(user);
            assertThat(personalMatchJoin.getMatching()).isEqualTo(matching);
        }
    }
}
