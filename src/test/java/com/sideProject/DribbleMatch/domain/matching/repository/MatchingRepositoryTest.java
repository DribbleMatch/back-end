package com.sideProject.DribbleMatch.domain.matching.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingType;
import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MatchingRepositoryTest {

    @Autowired
    private MatchingRepository matchingRepository;

    private Matching initMatching(String name) {
        return Matching.builder()
                .name(name)
                .people(10)
                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                .place("서울")
                .status(MatchingStatus.RECRUITING)
                .type(MatchingType.TEAM)
                .build();
    }
    @Nested
    @DisplayName("CreateMatchingTest")
    public class CreateMatchingTest {

        @DisplayName("Matching을 생성한다")
        @Test
        public void createMatching() {

            // given
            Matching matching = initMatching("testMatching");

            // when
            Matching savedMatching = matchingRepository.save(matching);

            // given
            assertThat(savedMatching).isNotNull();
            assertThat(savedMatching.getName()).isEqualTo("testMatching");
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createMatching2() {

            // given
            Matching matching = Matching.builder()
//                    .name("testMatching")
                    .people(10)
                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
//                    .place("서울")
                    .status(MatchingStatus.RECRUITING)
                    .type(MatchingType.TEAM)
                    .build();

            // when, then
            assertThatThrownBy(() -> matchingRepository.save(matching))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("이름이 입력되지 않았습니다.")
                    .contains("장소가 입력되지 않았습니다.");
        }
    }

    @Nested
    @DisplayName("SelectMatchingTest")
    public class SelectMatchingTest {

        @DisplayName("Matching을 조회한다")
        @Test
        public void selectMatching() {

            // given
            Matching savedMatching = matchingRepository.save(initMatching("testMatching"));

            // when
            Matching selectedMatching = matchingRepository.findById(savedMatching.getId()).get();

            // then
            assertThat(selectedMatching).isNotNull();
            assertThat(selectedMatching).isEqualTo(savedMatching);
        }

        @DisplayName("모든 Matching을 조회한다")
        @Test
        public void selectMatching2() {
            // given
            Matching savedMatching1 = matchingRepository.save(initMatching("testMatching1"));
            Matching savedMatching2 = matchingRepository.save(initMatching("testMatching2"));

            // when
            List<Matching> matchings = matchingRepository.findAll();

            // then
            assertThat(matchings.size()).isEqualTo(2);
            assertThat(matchings.contains(savedMatching1)).isTrue();
            assertThat(matchings.contains(savedMatching2)).isTrue();
        }

        @DisplayName("없는 Matching이면 에러가 발생한다")
        @Test
        public void selectMatching3() {
            // given
            Matching savedMatching = matchingRepository.save(initMatching("testMatching"));

            // when, then
            assertThatThrownBy(() -> matchingRepository.findById(savedMatching.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_MATCHING_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 경기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteMatchingTest")
    public class DeleteMatchingTest {

        @DisplayName("Matching을 삭제한다")
        @Test
        public void deleteMatching() {

            // given
            Matching savedMatching = matchingRepository.save(initMatching("testMatching"));

            // when
            matchingRepository.deleteById(savedMatching.getId());

            // then
            assertThat(matchingRepository.findById(savedMatching.getId()).isPresent()).isFalse();

        }
    }
}
