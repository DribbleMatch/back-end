package com.sideProject.DribbleMatch.domain.team.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    private Team initTeam(String name) {
        return Team.builder()
                .name(name)
                .region("서울")
                .winning(10)
                .build();
    }

    @Nested
    @DisplayName("createTeamTest")
    public class createTeamTest {

        @DisplayName("Team을 생성한다")
        @Test
        public void createTeam() {

            // given
            Team team = initTeam("test");

            // when
            Team savedTeam = teamRepository.save(team);

            // then
            assertThat(savedTeam).isNotNull();
            assertThat(savedTeam.getName()).isEqualTo("test");
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createTeam2() {

            // given
            Team team = Team.builder()
//                    .name("test")
//                    .region("서울")
                    .winning(10)
                    .build();

            // when, then
            assertThatThrownBy(() -> teamRepository.save(team))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("이름이 입력되지 않았습니다.")
                    .contains("지역이 입력되지 않았습니다.");
        }
    }

    @Nested
    @DisplayName("selectTeamTest")
    public class selectTeamTest {

        @DisplayName("Team을 조회한다")
        @Test
        public void selectTeam() {

            // given
            Team savedTeam = teamRepository.save(initTeam("test"));

            // when
            Team selectedTeam = teamRepository.findById(savedTeam.getId()).get();

            // then
            assertThat(selectedTeam).isNotNull();
            assertThat(selectedTeam).isEqualTo(savedTeam);
        }

        @DisplayName("모든 Team을 조회한다")
        @Test
        public void selectTeam2() {

            // given
            Team savedTeam1 = teamRepository.save(initTeam("test1"));
            Team savedTeam2 = teamRepository.save(initTeam("test2"));

            // when
            List<Team> teams = teamRepository.findAll();

            // then
            assertThat(teams.size()).isEqualTo(2);
            assertThat(teams.contains(savedTeam1)).isTrue();
            assertThat(teams.contains(savedTeam2)).isTrue();
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void selectTeam3() {

            // given
            Team savedTeam = teamRepository.save(initTeam("test"));

            // when, then
            assertThatThrownBy(() -> teamRepository.findById(savedTeam.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.INVALID_TEAM_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteTeamTest")
    public class deleteTeamTest {

        @DisplayName("deleteTeam")
        @Test
        public void deleteTeam() {

            // given
            Team savedTeam = teamRepository.save(initTeam("test"));

            // when
            teamRepository.deleteById(savedTeam.getId());

            // then
            assertThat(teamRepository.findById(savedTeam.getId()).isPresent()).isFalse();
        }
    }
}
