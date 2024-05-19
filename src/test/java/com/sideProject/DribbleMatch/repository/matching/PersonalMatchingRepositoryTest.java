package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class PersonalMatchingRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private PersonalMatchingRepository personalMatchingRepository;

    private Region initRegion(String dong) {
        return regionRepository.save(Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build());
    }

    private PersonalMatching initPersonalMatching(String name, Region region) {
        return PersonalMatching.builder()
                .name(name)
                .playPeople(5)
                .maxPeople(7)
                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                .status(MatchingStatus.RECRUITING)
                .region(region)
                .build();
    }
    @Nested
    @DisplayName("CreatePersonalMatchingTest")
    public class CreatePersonalMatchingTest {

        @DisplayName("PersonalMatching을 생성한다")
        @Test
        public void createPersonalMatching() {

            // given
            Region region = initRegion("당산동");
            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);

            // when
            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(personalMatching);

            // given
            assertThat(savedPersonalMatching).isNotNull();
            assertThat(savedPersonalMatching).isEqualTo(personalMatching);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createPersonalMatching2() {

            // given
            Region region = initRegion("당산동");

            PersonalMatching personalMatching = PersonalMatching.builder()
//                    .name("testPersonalMatching")
                    .playPeople(5)
                    .maxPeople(7)
                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                    .status(MatchingStatus.RECRUITING)
                    .region(region)
                    .build();

            // when, then
            assertThatThrownBy(() -> personalMatchingRepository.save(personalMatching))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectPersonalMatchingTest")
    public class SelectPersonalMatchingTest {

        @DisplayName("PersonalMatching을 조회한다")
        @Test
        public void selectPersonalMatching() {

            // given
            Region region = initRegion("당산동");

            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching", region));

            // when
            PersonalMatching selectedPersonalMatching = personalMatchingRepository.findById(savedPersonalMatching.getId()).get();

            // then
            assertThat(selectedPersonalMatching).isNotNull();
            assertThat(selectedPersonalMatching).isEqualTo(savedPersonalMatching);
        }

        @DisplayName("모든 PersonalMatching을 조회한다")
        @Test
        public void selectPersonalMatching2() {
            // given
            Region region = initRegion("당산동");

            PersonalMatching savedPersonalMatching1 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching1", region));
            PersonalMatching savedPersonalMatching2 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching2", region));

            // when
            List<PersonalMatching> personalMatchings = personalMatchingRepository.findAll();

            // then
            assertThat(personalMatchings.size()).isEqualTo(2);
            assertThat(personalMatchings.contains(savedPersonalMatching1)).isTrue();
            assertThat(personalMatchings.contains(savedPersonalMatching2)).isTrue();
        }

        @DisplayName("없는 PersonalMatching이면 에러가 발생한다")
        @Test
        public void selectPersonalMatching3() {
            // given
            Region region = initRegion("당산동");

            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching", region));

            // when, then
            assertThatThrownBy(() -> personalMatchingRepository.findById(savedPersonalMatching.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_PERSONAL_MATCHING_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 개인 경기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeletePersonalMatchingTest")
    public class DeletePersonalMatchingTest {

        @DisplayName("PersonalMatching을 삭제한다")
        @Test
        public void deletePersonalMatching() {

            // given
            Region region = initRegion("당산동");
            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching", region));

            // when
            personalMatchingRepository.deleteById(savedPersonalMatching.getId());

            // then
            assertThat(personalMatchingRepository.findById(savedPersonalMatching.getId()).isPresent()).isFalse();
        }
    }
}
