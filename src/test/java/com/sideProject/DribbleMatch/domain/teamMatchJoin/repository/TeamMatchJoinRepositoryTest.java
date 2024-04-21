package com.sideProject.DribbleMatch.domain.teamMatchJoin.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingType;
import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import com.sideProject.DribbleMatch.domain.matching.repository.MatchingRepository;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.team.repository.TeamRepository;
import com.sideProject.DribbleMatch.domain.teamMatchJoin.entity.TeamMatchJoin;
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
public class TeamMatchJoinRepositoryTest {

    @Autowired
    private TeamMatchJoinRepository teamMatchJoinRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchingRepository matchingRepository;

    private Team initTeam(String name) {
        return teamRepository.save(Team.builder()
                .name(name)
                .region("서울")
                .winning(10)
                .build());
    }

    private Matching initMatching(String name) {
        return matchingRepository.save(Matching.builder()
                .name(name)
                .people(10)
                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                .place("서울")
                .status(MatchingStatus.RECRUITING)
                .type(MatchingType.TEAM)
                .build());
    }

    private TeamMatchJoin initTeamMatchJoin(Team team, Matching matching) {
        return TeamMatchJoin.builder()
                .team(team)
                .matching(matching)
                .build();
    }
    @Nested
    @DisplayName("createTeamMatchJoinTest")
    public class createTeamMatchJoinTest {

        @DisplayName("TeamMatchJoin를 생성한다")
        @Test
        public void createTeamMatchJoin() {

            // given
            Team team = initTeam("testTeam");
            Matching matching = initMatching("testMatching");
            TeamMatchJoin teamMatchJoin = initTeamMatchJoin(team, matching);

            // when
            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(teamMatchJoin);

            // then
            assertThat(savedTeamMatchJoin).isNotNull();
            assertThat(savedTeamMatchJoin).isEqualTo(teamMatchJoin);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createTeamMatchJoin2() {

            // given
            Team team = initTeam("testTeam");
            Matching matching = initMatching("testMatching");
            TeamMatchJoin teamMatchJoin = initTeamMatchJoin(team, null);

            // when, then
            assertThatThrownBy(() -> teamMatchJoinRepository.save(teamMatchJoin))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("경기가 입력되지 않았습니다.");
        }
    }

    @Nested
    @DisplayName("selectTeamMatchJoinTest")
    public class selectTeamMatchJoinTest {

        @DisplayName("TeamMatchJoin을 조회한다")
        @Test
        public void selectTeamMatchJoin() {

            // given
            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(initTeamMatchJoin(initTeam("testTeam"), initMatching("testMatching")));

            // when
            TeamMatchJoin selectedTeamMatchJoin = teamMatchJoinRepository.findById(savedTeamMatchJoin.getId()).get();

            // then
            assertThat(selectedTeamMatchJoin).isNotNull();
            assertThat(selectedTeamMatchJoin).isEqualTo(savedTeamMatchJoin);
        }

        @DisplayName("모든 TeamMatchJoin을 조회한다")
        @Test
        public void selectTeamMatchJoin2() {

            // given
            TeamMatchJoin savedTeamMatchJoin1 = teamMatchJoinRepository.save(initTeamMatchJoin(initTeam("testTeam1"), initMatching("testMatching1")));
            TeamMatchJoin savedTeamMatchJoin2 = teamMatchJoinRepository.save(initTeamMatchJoin(initTeam("testTeam2"), initMatching("testMatching2")));

            // when
            List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findAll();

            // then
            assertThat(teamMatchJoins.size()).isEqualTo(2);
            assertThat(teamMatchJoins.contains(savedTeamMatchJoin1)).isTrue();
            assertThat(teamMatchJoins.contains(savedTeamMatchJoin2)).isTrue();
        }

        @DisplayName("없는 TeamMatchJoin이면 에러가 발생한다")
        @Test
        public void selectTeamMatchJoin3() {

            // given
            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(initTeamMatchJoin(initTeam("testTeam"), initMatching("testMatching")));

            // when, then
            assertThatThrownBy(() -> teamMatchJoinRepository.findById(savedTeamMatchJoin.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_TEAM_MATCH_JOIN_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀경기 참가 정보가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteTeamMatchJoinTest")
    public class deleteTeamMatchJoinTest {

        @DisplayName("TeamMatchJoin를 삭제한다")
        @Test
        public void deleteTeamMatchJoin() {

            // given
            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(initTeamMatchJoin(initTeam("testTeam"), initMatching("testMatching")));

            // when
            teamMatchJoinRepository.deleteById(savedTeamMatchJoin.getId());

            // then
            assertThat(teamMatchJoinRepository.findById(savedTeamMatchJoin.getId()).isPresent()).isFalse();
        }
    }
}
