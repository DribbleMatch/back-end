package com.sideProject.DribbleMatch.domain.userTeam.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.team.repository.TeamRepository;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import com.sideProject.DribbleMatch.domain.user.repository.UserRepository;
import com.sideProject.DribbleMatch.domain.userTeam.entity.UserTeam;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserTeamRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserTeamRepository userTeamRepository;

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

    private Team initTeam(String name) {
        return teamRepository.save(Team.builder()
                .name(name)
                .region("서울")
                .winning(10)
                .leader(initUser("test@test.com", "test"))
                .build());
    }

    private UserTeam initUserTeam(User user, Team team) {
        return UserTeam.builder()
                .user(user)
                .team(team)
                .build();
    }

    @Nested
    @DisplayName("createUserTeamTest")
    public class createUserTeamTest {

        @DisplayName("Team을 생성한다")
        @Test
        public void createUserTeam() {

            // given
            User user = initUser("test1@test.com", "test1");
            Team team = initTeam("testTeam");
            UserTeam userTeam = initUserTeam(user, team);

            // when
            UserTeam savedUserTeam = userTeamRepository.save(userTeam);

            // then
            assertThat(savedUserTeam).isNotNull();
            assertThat(savedUserTeam.getUser()).isEqualTo(user);
            assertThat(savedUserTeam.getTeam()).isEqualTo(team);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createUserTeam2() {

            // given
            User user = initUser("test@test.com", "test");
            UserTeam userTeam = initUserTeam(user, null);

            // when, then
            assertThatThrownBy(() -> userTeamRepository.save(userTeam))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("팀이 입력되지 않았습니다.");
        }
    }

    @Nested
    @DisplayName("selectUserTeamTest")
    public class selectUserTeamTest {

        @DisplayName("UserTeam을 조회한다")
        @Test
        public void selectUserTeam() {

            // given
            UserTeam savedUserTeam = userTeamRepository.save(initUserTeam(initUser("test1@test.com", "test1"), initTeam("testTeam")));

            // when
            UserTeam selectedUserTeam = userTeamRepository.findById(savedUserTeam.getId()).get();

            // then
            assertThat(selectedUserTeam).isNotNull();
            assertThat(selectedUserTeam).isEqualTo(savedUserTeam);
        }

        @DisplayName("모든 UserTeam을 조회한다")
        @Test
        public void selectUserTeam2() {

            // given
            Team team = initTeam("testTeam");
            UserTeam savedUserTeam1 = userTeamRepository.save(initUserTeam(initUser("test1@test.com", "test1"), team));
            UserTeam savedUserTeam2 = userTeamRepository.save(initUserTeam(initUser("test2@test.com", "test2"), team));

            // when
            List<UserTeam> userTeams = userTeamRepository.findAll();

            // then
            assertThat(userTeams.size()).isEqualTo(2);
            assertThat(userTeams.contains(savedUserTeam1)).isTrue();
            assertThat(userTeams.contains(savedUserTeam2)).isTrue();
        }

        @DisplayName("없는 UserTeam이면 에러가 발생한다")
        @Test
        public void selectUserTeam3() {

            // given
            UserTeam savedUserTeam = userTeamRepository.save(initUserTeam(initUser("test1@test.com", "test1"), initTeam("testTeam")));

            // when, then
            assertThatThrownBy(() -> userTeamRepository.findById(savedUserTeam.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_USERTEAM_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 소속팀 정보가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteUserTeamTest")
    public class deleteUserTeamTest {

        @DisplayName("UserTeam을 삭제한다")
        @Test
        public void deleteUserTeam() {

            // given
            User user = initUser("test1@test.com", "test1");
            UserTeam savedUserTeam = userTeamRepository.save(initUserTeam(user, initTeam("testTeam")));

            // when
            userRepository.deleteById(savedUserTeam.getId());

            // then
            assertThat(userRepository.findById(savedUserTeam.getId()).isPresent()).isFalse();
        }
    }
}
