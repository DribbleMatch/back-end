package com.sideProject.DribbleMatch.repository.region;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.region.Region;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
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
                .eupMyeonLeeDong("")
                .lee("")
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

            Region region1 = regionRepository.save(initRegion(""));
            Region region2 = regionRepository.save(initRegion("당산동"));
            Region region3 = regionRepository.save(initRegion("문래동"));
            Region region4 = regionRepository.save(initRegion(""));

            ReflectionTestUtils.setField(region4, "siGunGu", "마포구");

            // when
            Region selectedRegion1 = regionRepository.findByRegionString("서울특별시 영등포구").get();
            Region selectedRegion2 = regionRepository.findByRegionString("서울특별시 영등포구 당산동").get();
            Region selectedRegion3 = regionRepository.findByRegionString("서울특별시 영등포구 문래동").get();
            Region selectedRegion4 = regionRepository.findByRegionString("서울특별시 마포구").get();

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
            assertThatThrownBy(() -> regionRepository.findByRegionString("서울특별시 영등포구 당산동").orElseThrow(() ->
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
            Region region1 = regionRepository.save(initRegion("당산동"));
            Region region2 = regionRepository.save(initRegion("문래동"));
            ReflectionTestUtils.setField(region2, "lee", "두북리");

            // when
            String regionString1 = regionRepository.findRegionStringById(region1.getId()).get();
            String regionString2 = regionRepository.findRegionStringById(region2.getId()).get();

            // then
            assertThat(regionString1).isEqualTo("서울특별시 영등포구 당산동");
            assertThat(regionString2).isEqualTo("서울특별시 영등포구 문래동 두북리");
        }

        @DisplayName("Region Id가 없으면 에러가 발생한다")
        @Test
        public void findRegionStringById2() {

            // given
            Region region = regionRepository.save(initRegion("당산동"));

            // when, then
            assertThatThrownBy(() -> regionRepository.findRegionStringById(region.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_REGION_ID)))
                    .hasMessage("해당 지역이 존재하지 않습니다.")
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    @DisplayName("FindIdsByRegionStringTest")
    public class FindIdsByRegionStringTest {

        @DisplayName("입력된 매개변수에 맞는 Region들의 Id를 조회한다")
        @Test
        public void findIdsByRegionString() {

            // given
            Region region1 = regionRepository.save(initRegion("당산동"));
            Region region2 = regionRepository.save(initRegion("문래동"));
            Region region3 = regionRepository.save(initRegion("양평동"));
            Region region4 = regionRepository.save(initRegion("합정동"));

            ReflectionTestUtils.setField(region4, "siGunGu", "마포구");

            String regionString1 = "서울특별시 영등포구";
            String regionString2 = "서울특별시 마포구";
            String regionString3 = "서울특별시 영등포구 당산동";

            // when
            List<Long> result1 = regionRepository.findIdsByRegionString(regionString1);
            List<Long> result2 = regionRepository.findIdsByRegionString(regionString2);
            List<Long> result3 = regionRepository.findIdsByRegionString(regionString3);

            // then
            assertThat(result1.size()).isEqualTo(3);
            assertThat(result1.contains(region1.getId())).isTrue();
            assertThat(result1.contains(region2.getId())).isTrue();
            assertThat(result1.contains(region3.getId())).isTrue();

            assertThat(result2.size()).isEqualTo(1);
            assertThat(result2.contains(region4.getId())).isTrue();

            assertThat(result3.size()).isEqualTo(1);
            assertThat(result3.contains(region1.getId())).isTrue();
        }
    }
}
