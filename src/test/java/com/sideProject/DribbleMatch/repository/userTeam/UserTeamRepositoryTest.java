package com.sideProject.DribbleMatch.repository.userTeam;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.entity.userTeam.UserTeam;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class UserTeamRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserTeamRepository userTeamRepository;

    private Region initRegion(String dong) {
        return regionRepository.save(Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build());
    }

    private User initUser(String email, String name, Region region) {
        return userRepository.save(User.builder()
                .email(email)
                .password("test1234!A")
                .nickName(name)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .region(region)
                .build());
    }

    private Team initTeam(String name, User leader, Region region) {
        return teamRepository.save(Team.builder()
                .name(name)
                .winning(10)
                .leader(leader)
                .region(region)
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
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

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
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            UserTeam userTeam = UserTeam.builder()
                    .user(user)
//                    .team(team)
                    .build();

            // when, then
            assertThatThrownBy(() -> userTeamRepository.save(userTeam))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("selectUserTeamTest")
    public class selectUserTeamTest {

        @DisplayName("UserTeam을 조회한다")
        @Test
        public void selectUserTeam() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            UserTeam savedUserTeam = userTeamRepository.save(initUserTeam(user, team));

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
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            UserTeam savedUserTeam1 = userTeamRepository.save(initUserTeam(user, team));
            UserTeam savedUserTeam2 = userTeamRepository.save(initUserTeam(user, team));

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
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            UserTeam savedUserTeam = userTeamRepository.save(initUserTeam(user, team));

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
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            UserTeam savedUserTeam = userTeamRepository.save(initUserTeam(user, team));

            // when
            userRepository.deleteById(savedUserTeam.getId());

            // then
            assertThat(userRepository.findById(savedUserTeam.getId()).isPresent()).isFalse();
        }
    }
}
