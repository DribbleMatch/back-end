package com.sideProject.DribbleMatch.entity.statium;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StadiumTest {

    public Region initRegion(String dong) {
        return Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .build();
    }

    @Nested
    @DisplayName("BuilderTest")
    public class BuilderTest {

        @DisplayName("Stadium을 생성한다")
        @Test
        public void createStadium() {

            // given
            Region region = initRegion("당산동");

            // when
            Stadium stadium = Stadium.builder()
                    .name("testStadium")
                    .region(region)
                    .detailAddress("1-1")
                    .rentalFee(100000)
                    .build();

            // then
            assertThat(stadium.getName()).isEqualTo("testStadium");
            assertThat(stadium.getRegion()).isEqualTo(region);
            assertThat(stadium.getDetailAddress()).isEqualTo("1-1");
            assertThat(stadium.getRentalFee()).isEqualTo(100000);
        }
    }
}
