package com.sideProject.DribbleMatch.repository.region;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.region.Region;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RegionRepositoryTest {

    @Autowired
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
    @DisplayName("CreateRegionTest")
    public class CreateRegionTest {

        @DisplayName("Region을 저장한다")
        @Test
        public void createRegionTest() {

            // given
            Region region = initRegion("당산동");

            // when
            Region savedRegion = regionRepository.save(region);

            // then
            assertThat(savedRegion).isEqualTo(region);
        }

        @DisplayName("NotNull인 컬럼이 null 이면 에러가 발생한다")
        @Test
        public void createRegionTest2() {

            // given
            Region region = Region.builder().build();

            // when, then
            assertThatThrownBy(() -> regionRepository.save(region))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectRegionTest")
    public class SelectRegionTest {

        @DisplayName("Region을 조회한다")
        @Test
        public void selectRegionTest() {

            // given
            Region savedRegion = regionRepository.save(initRegion("당산동"));

            // when
            Region selectedRegion = regionRepository.findById(savedRegion.getId()).get();

            // then
            assertThat(selectedRegion).isEqualTo(savedRegion);
        }

        @DisplayName("String으로 입력 받은 주소로 Region을 조회한다")
        @Test
        public void selectRegionTest2() {

            // given

            Region region1 = regionRepository.save(initRegion(null));
            Region region2 = regionRepository.save(initRegion("당산동"));
            Region region3 = regionRepository.save(initRegion("문래동"));
            Region region4 = regionRepository.save(Region.builder()
                    .siDo("서울특별시")
                    .siGunGu("마포구")
                    .build());

            // when
            Region selectedRegion1 = regionRepository.findByRegionString("서울특별시", "영등포구", null, null, null).get();
            Region selectedRegion2 = regionRepository.findByRegionString("서울특별시", "영등포구", "당산동", null, null).get();
            Region selectedRegion3 = regionRepository.findByRegionString("서울특별시", "영등포구", "문래동", null, null).get();
            Region selectedRegion4 = regionRepository.findByRegionString("서울특별시", "마포구", null, null, null).get();

            // then

            assertThat(selectedRegion1).isEqualTo(region1);
            assertThat(selectedRegion2).isEqualTo(region2);
            assertThat(selectedRegion3).isEqualTo(region3);
            assertThat(selectedRegion4).isEqualTo(region4);
        }

        @DisplayName("없는 Region이면 에러가 발생한다")
        @Test
        public void selectRegionTest3() {

            // given
            Region savedRegion = regionRepository.save(initRegion("당산동"));

            // when, then
            assertThatThrownBy(() -> regionRepository.findById(savedRegion.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_REGION_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 지역이 존재하지 않습니다.");
        }

        @DisplayName("String으로 입력 받은 주소가 없는 Region이면 에러가 발생한다")
        @Test
        public void selectRegionTest4() {

            // given
            Region region = regionRepository.save(initRegion(null));

            // when, then
            assertThatThrownBy(() -> regionRepository.findByRegionString("서울특별시", "영등포구", "당산동", null, null).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_REGION_STRING)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 지역 문자열에 해당하는 지역이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("FindRegionStringByIdTest")
    public class FindRegionStringByIdTest {

        @DisplayName("Region Id로 합쳐진 문자열을 찾는다")
        @Test
        public void findRegionStringById() {

            // given
            Region region = regionRepository.save(initRegion("당산동"));

            // when
            String regionString = regionRepository.findRegionStringById(region.getId());

            // then
            assertThat(regionString.trim()).isEqualTo("서울특별시 영등포구 당산동");
        }
    }
}
