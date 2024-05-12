package com.sideProject.DribbleMatch.repository.Stadium;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.stadium.StadiumRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class StadiumRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    private Region initRegion(String dong) {
        return regionRepository.save(Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build());
    }

    private Stadium initStadium(String name, Region region) {
        return Stadium.builder()
                .name(name)
                .region(region)
                .detailAddress("1-1")
                .rentalFee(100000)
                .build();
    }

    @Nested
    @DisplayName("CreateStadiumTest")
    public class CreateStadiumTest {

        @DisplayName("Stadium을 생성한다")
        @Test
        public void createStadium() {

            // given
            Region region = initRegion("당산동");

            Stadium stadium = initStadium("testStadium", region);

            // when
            Stadium savedStadium = stadiumRepository.save(stadium);

            // then
            assertThat(savedStadium).isNotNull();
            assertThat(savedStadium).isEqualTo(stadium);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createStadium2() {

            // given
            Region region = initRegion("당산동");

            Stadium stadium = initStadium("testStadium", null);

            // when, then
            assertThatThrownBy(() -> stadiumRepository.save(stadium))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectStadiumTest")
    public class SelectStadiumTest {

        @DisplayName("Stadium을 조회한다")
        @Test
        public void selectStadium() {

            // given
            Region region = initRegion("당산동");

            Stadium savedStadium = stadiumRepository.save(initStadium("testStadium", region));

            // when
            Stadium selectdStadium = stadiumRepository.findById(savedStadium.getId()).get();

            // then
            assertThat(selectdStadium).isNotNull();
            assertThat(selectdStadium).isEqualTo(savedStadium);
        }

        @DisplayName("모든 Stadium을 조회한다")
        @Test
        public void selectStadium2() {

            // given
            Region region = initRegion("당산동");

            Stadium savedStadium1 = stadiumRepository.save(initStadium("testStadium1", region));
            Stadium savedStadium2 = stadiumRepository.save(initStadium("testStadium2", region));

            // when
            List<Stadium> stadiums = stadiumRepository.findAll();

            // then
            assertThat(stadiums.size()).isEqualTo(2);
            assertThat(stadiums.contains(savedStadium1)).isTrue();
            assertThat(stadiums.contains(savedStadium2)).isTrue();
        }

        @DisplayName("없는 Stadium을면 에러가 발생한다")
        @Test
        public void selectStadium3() {

            // given
            Region region = initRegion("당산동");

            Stadium savedStadium = stadiumRepository.save(initStadium("testStadium", region));

            // when, then
            assertThatThrownBy(() -> stadiumRepository.findById(savedStadium.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_STADIUM_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 경기장이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteStadiumTest")
    public class DeleteStadiumTest {

        @DisplayName("Stadium을 삭제한다")
        @Test
        public void deleteStadium() {

            // given
            Region region = initRegion("당산동");

            Stadium savedStadium = stadiumRepository.save(initStadium("testStadium", region));

            // when
            stadiumRepository.deleteById(savedStadium.getId());

            // then
            assertThat(stadiumRepository.findById(savedStadium.getId()).isPresent()).isFalse();
        }
    }
}
