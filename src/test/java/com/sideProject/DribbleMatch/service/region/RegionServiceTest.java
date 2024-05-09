package com.sideProject.DribbleMatch.service.region;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {
    
    @InjectMocks
    private RegionServiceImpl regionService;
    
    @Mock
    private RegionRepository regionRepository;

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
    @DisplayName("FindRegionTest")
    public class FindRegionTest {

        @DisplayName("입력받은 주소로 Region을 반환한다")
        @Test
        public void findRegion() {

            // given
            String regionString = "서울특별시 영등포구 당산동";
            Region region = initRegion("당산동");

            // mocking
            when(regionRepository.findByRegionString("서울특별시 영등포구 당산동")).thenReturn(Optional.ofNullable(region));

            // when
            Region foundRegion = regionService.findRegion("서울특별시 영등포구 당산동");

            // then
            assertThat(foundRegion).isEqualTo(region);
        }

        @DisplayName("입력받은 주소가 없는 주소이면 에러가 발생한다")
        @Test
        public void findRegion2() {

            // mocking
            when(regionRepository.findByRegionString("서울특별시 영등포구 당산동")).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> regionService.findRegion("서울특별시 영등포구 당산동"))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 지역 문자열에 해당하는 지역이 존재하지 않습니다.");

        }
    }
}
