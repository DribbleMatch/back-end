package com.sideProject.DribbleMatch.domain.team.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import com.sideProject.DribbleMatch.domain.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    private User initUser(String email, String nickName) {
        return userRepository.save(User.builder()
                .email(email)
                .password("test1234")
                .nickName(nickName)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .build());
    }

    private Team initTeam(String name, User leader) {
        return Team.builder()
                .name(name)
                .region("서울")
                .winning(10)
                .leader(leader)
                .build();
    }

    @Nested
    @DisplayName("CreateTeamTest")
    public class CreateTeamTest {

        @DisplayName("Team을 생성한다")
        @Test
        public void createTeam() {

            // given
            Team team = initTeam("testTeam", initUser("test@test.com", "test"));

            // when
            Team savedTeam = teamRepository.save(team);

            // then
            assertThat(savedTeam).isNotNull();
            assertThat(savedTeam.getName()).isEqualTo("testTeam");
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createTeam2() {

            // given
            Team team = Team.builder()
//                    .name("testTeam")
//                    .region("서울")
                    .winning(10)
                    .leader(initUser("test@test.com", "test"))
                    .build();

            // when, then
            assertThatThrownBy(() -> teamRepository.save(team))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("이름이 입력되지 않았습니다.")
                    .contains("지역이 입력되지 않았습니다.");
        }

        @DisplayName("@name이 중복이면 에러가 발생한다")
        @Test
        public void createTeam3() {

            // given
            User leader = initUser("test@test.com", "test");
            Team team1 = Team.builder()
                    .name("testTeam")
                    .region("서울")
                    .winning(10)
                    .leader(leader)
                    .build();
            Team team2 = Team.builder()
                    .name("testTeam")
                    .region("서울")
                    .winning(10)
                    .leader(leader)
                    .build();

            // when
            teamRepository.save(team1);

            // then
            assertThatThrownBy(() -> teamRepository.save(team2))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectTeamTest")
    public class SelectTeamTest {

        @DisplayName("Team을 조회한다")
        @Test
        public void selectTeam() {

            // given
            Team savedTeam = teamRepository.save(initTeam("testTeam", initUser("test@test.com", "test")));

            // when
            Team selectedTeam = teamRepository.findById(savedTeam.getId()).get();

            // then
            assertThat(selectedTeam).isNotNull();
            assertThat(selectedTeam).isEqualTo(savedTeam);
        }

        @DisplayName("모든 Team을 조회한다 (Page)")
        @Test
        public void selectTeam2() {

            // given
            User leader = initUser("test@test.com", "test");
            Team savedTeam1 = teamRepository.save(initTeam("testTeam1", leader));
            Team savedTeam2 = teamRepository.save(initTeam("testTeam2", leader));
            Team savedTeam3 = teamRepository.save(initTeam("testTeam3", leader));
            Team savedTeam4 = teamRepository.save(initTeam("testTeam4", leader));

            Pageable pageable = PageRequest.of(1, 2);

            // when
            Page<Team> teams = teamRepository.findAll(pageable);

            // then
            assertThat(teams.getTotalPages()).isEqualTo(2);
            assertThat(teams.getTotalElements()).isEqualTo(4);
            assertThat(teams.getContent().size()).isEqualTo(2);

            assertThat(teams.get().toList().get(0)).isEqualTo(savedTeam3);
        }

        @DisplayName("지역별로 Team을 조회한다 (Page)")
        @Test
        public void selectTeam3() {

            //todo: 지역별 조회 구현 후 작성
        }

        @DisplayName("Name으로 Team을 조회한다")
        @Test
        public void selectTeam4() {

            // given
            Team savedTeam = teamRepository.save(initTeam("testTeam", initUser("test@test.com", "test")));

            // when
            Team selectedTeam = teamRepository.findByName(savedTeam.getName()).get();

            // then
            assertThat(selectedTeam).isNotNull();
            assertThat(selectedTeam).isEqualTo(savedTeam);
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void selectTeam5() {

            // given
            Team savedTeam = teamRepository.save(initTeam("testTeam", initUser("test@test.com", "test")));

            // when, then
            assertThatThrownBy(() -> teamRepository.findById(savedTeam.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("UpdateTeamTest")
    public class UpdateTeamTest {

        @DisplayName("팀 정보를 수정한다")
        @Test
        public void updateTeam() {

            // given
            Team savedTeam = teamRepository.save(initTeam("testTeam1", initUser("test1@test.com", "test1")));
            User newLeader = initUser("test2@test.com", "test");

            // when
            ReflectionTestUtils.setField(savedTeam, "name", "testTeam2");
            ReflectionTestUtils.setField(savedTeam, "region", "부산");
            ReflectionTestUtils.setField(savedTeam, "winning", 11);
            ReflectionTestUtils.setField(savedTeam, "leader", newLeader);


            Team updatedTeam = teamRepository.findById(savedTeam.getId()).get();

            // then
            assertThat(updatedTeam.getName()).isEqualTo("testTeam2");
            assertThat(updatedTeam.getRegion()).isEqualTo("부산");
            assertThat(updatedTeam.getWinning()).isEqualTo(11);
            assertThat(updatedTeam.getLeader()).isEqualTo(newLeader);
        }
    }

    @Nested
    @DisplayName("DeleteTeamTest")
    public class DeleteTeamTest {

        @DisplayName("팀을 삭제한다")
        @Test
        public void deleteTeam() {

            // given
            Team savedTeam = teamRepository.save(initTeam("testTeam", initUser("test@test.com", "test")));

            // when
            teamRepository.deleteById(savedTeam.getId());

            // then
            assertThat(teamRepository.findById(savedTeam.getId()).isPresent()).isFalse();
        }
    }
}
