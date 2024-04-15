package com.sideProject.DribbleMatch.domain.matching;

import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingType;
import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchingTest {

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("createMatching")
        @Test
        public void createMatching() {

            // given, when
            Matching matching = Matching.builder()
                    .name("test")
                    .people(10)
                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                    .place("서울")
                    .status(MatchingStatus.RECRUITING)
                    .type(MatchingType.TEAM)
                    .build();

            // then
            assertThat(matching.getName()).isEqualTo("test");
            assertThat(matching.getPeople()).isEqualTo(10);
            assertThat(matching.getStartAt()).isEqualTo(LocalDateTime.of(2001, 1, 1, 12, 0));
            assertThat(matching.getEndAt()).isEqualTo(LocalDateTime.of(2001, 1, 1, 14, 0));
            assertThat(matching.getStatus()).isEqualTo(MatchingStatus.RECRUITING);
            assertThat(matching.getType()).isEqualTo(MatchingType.TEAM);
        }
    }
}
