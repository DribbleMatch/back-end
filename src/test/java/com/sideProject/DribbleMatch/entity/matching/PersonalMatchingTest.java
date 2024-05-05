package com.sideProject.DribbleMatch.entity.matching;

import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.region.Region;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonalMatchingTest {

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

        @DisplayName("PersonalMatching을 생성한다")
        @Test
        public void createPersonalMatching() {

            // given, when
            Region region = initRegion("당산동");
            PersonalMatching personalMatching = PersonalMatching.builder()
                    .name("test")
                    .playPeople(5)
                    .maxPeople(7)
                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                    .status(MatchingStatus.RECRUITING)
                    .region(region)
                    .build();

            // then
            assertThat(personalMatching.getName()).isEqualTo("test");
            assertThat(personalMatching.getPlayPeople()).isEqualTo(5);
            assertThat(personalMatching.getMaxPeople()).isEqualTo(7);
            assertThat(personalMatching.getStartAt()).isEqualTo(LocalDateTime.of(2001, 1, 1, 12, 0));
            assertThat(personalMatching.getEndAt()).isEqualTo(LocalDateTime.of(2001, 1, 1, 14, 0));
            assertThat(personalMatching.getStatus()).isEqualTo(MatchingStatus.RECRUITING);
            assertThat(personalMatching.getRegion()).isEqualTo(region);
        }
    }
}
