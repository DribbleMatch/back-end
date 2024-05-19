package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
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
public class teamMatchRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private TeamMatchingRepository teamMatchingRepository;

    private Region initRegion(String dong) {
        return regionRepository.save(Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build());
    }

    private TeamMatch initTeamMatching(String name, Region region) {
        return TeamMatch.builder()
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
    @DisplayName("CreateTeamMatchingTest")
    public class CreateTeamMatchTest {

        @DisplayName("TeamMatching을 생성한다")
        @Test
        public void createTeamMatching() {

            // given
            Region region = initRegion("당산동");
            TeamMatch teamMatching = initTeamMatching("testTeamMatching", region);

            // when
            TeamMatch savedTeamMatching = teamMatchingRepository.save(teamMatching);

            // given
            assertThat(savedTeamMatching).isNotNull();
            assertThat(savedTeamMatching).isEqualTo(teamMatching);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createTeamMatching2() {

            // given
            Region region = initRegion("당산동");

            TeamMatch teamMatching = TeamMatch.builder()
//                    .name("testTeamMatching")
                    .playPeople(5)
                    .maxPeople(7)
                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                    .status(MatchingStatus.RECRUITING)
                    .region(region)
                    .build();

            // when, then
            assertThatThrownBy(() -> teamMatchingRepository.save(teamMatching))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectTeamMatchingTest")
    public class SelectTeamMatchTest {

        @DisplayName("TeamMatching을 조회한다")
        @Test
        public void selectTeamMatching() {

            // given
            Region region = initRegion("당산동");

            TeamMatch savedTeamMatching = teamMatchingRepository.save(initTeamMatching("testTeamMatching", region));

            // when
            TeamMatch selectedTeamMatching = teamMatchingRepository.findById(savedTeamMatching.getId()).get();

            // then
            assertThat(selectedTeamMatching).isNotNull();
            assertThat(selectedTeamMatching).isEqualTo(savedTeamMatching);
        }

        @DisplayName("모든 TeamMatching을 조회한다")
        @Test
        public void selectTeamMatching2() {
            // given
            Region region = initRegion("당산동");

            TeamMatch savedTeamMatching1 = teamMatchingRepository.save(initTeamMatching("testTeamMatching1", region));
            TeamMatch savedTeamMatching2 = teamMatchingRepository.save(initTeamMatching("testTeamMatching2", region));

            // when
            List<TeamMatch> teamMatchings = teamMatchingRepository.findAll();

            // then
            assertThat(teamMatchings.size()).isEqualTo(2);
            assertThat(teamMatchings.contains(savedTeamMatching1)).isTrue();
            assertThat(teamMatchings.contains(savedTeamMatching2)).isTrue();
        }

        @DisplayName("없는 TeamMatching이면 에러가 발생한다")
        @Test
        public void selectTeamMatching3() {
            // given
            Region region = initRegion("당산동");

            TeamMatch savedTeamMatching = teamMatchingRepository.save(initTeamMatching("testTeamMatching", region));

            // when, then
            assertThatThrownBy(() -> teamMatchingRepository.findById(savedTeamMatching.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_TEAM_MATCHING_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀 경기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteTeamMatchingTest")
    public class DeleteTeamMatchTest {

        @DisplayName("TeamMatching을 삭제한다")
        @Test
        public void deleteTeamMatching() {

            // given
            Region region = initRegion("당산동");
            TeamMatch savedTeamMatching = teamMatchingRepository.save(initTeamMatching("testTeamMatching", region));

            // when
            teamMatchingRepository.deleteById(savedTeamMatching.getId());

            // then
            assertThat(teamMatchingRepository.findById(savedTeamMatching.getId()).isPresent()).isFalse();
        }
    }
}
