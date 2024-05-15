package com.sideProject.DribbleMatch.repository.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.JoinStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class TeamApplicationRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamApplicationRepository teamApplicationRepository;

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
                .info("test")
                .maxNumber(10)
                .build());
    }

    private TeamApplication initTeamApplication(User user, Team team) {
        return TeamApplication.builder()
                .user(user)
                .team(team)
                .introduce("testIntroduce")
                .build();
    }

    @Nested
    @DisplayName("CreateTeamApplicationTest")
    public class CreateTeamApplicationTest {

        @DisplayName("TeamApplication을 생성한다")
        @Test
        public void createTeamApplication() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);
            Team team = initTeam("testTeam", leader, region);
            User user = initUser("test2@test.com", "test2", region);

            TeamApplication teamApplication = initTeamApplication(user, team);

            // when
            TeamApplication savedTeamApplication = teamApplicationRepository.save(teamApplication);

            // then
            assertThat(savedTeamApplication).isNotNull();
            assertThat(savedTeamApplication).isEqualTo(teamApplication);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createTeamApplication2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            TeamApplication teamApplication = TeamApplication.builder()
                    .team(team)
                    .build();

            // when, then
            assertThatThrownBy(() -> teamApplicationRepository.save(teamApplication))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectTeamApplicationTest")
    public class SelectTeamApplicationTest {

        @DisplayName("TeamApplication을 조회한다")
        @Test
        public void selectTeamApplication() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);
            Team team = initTeam("testTeam", leader, region);
            User user = initUser("test2@test.com", "test2", region);

            TeamApplication savedTeamApplication = teamApplicationRepository.save(initTeamApplication(user, team));

            // when
            TeamApplication selectedApplication = teamApplicationRepository.findById(savedTeamApplication.getId()).get();

            // then
            assertThat(selectedApplication).isNotNull();
            assertThat(selectedApplication).isEqualTo(savedTeamApplication);
        }

        @DisplayName("모든 TeamApplication을 조회한다 (Page)")
        @Test
        public void selectTeamApplication2() {

            // given
            Region region1 = initRegion("당산동1");
            Region region2 = initRegion("당산동2");

            User leader1 = initUser("test1@test.com", "test1", region1);
            User leader2= initUser("test2@test.com", "test2", region2);
            User user1 = initUser("test3@test.com", "test3", region1);
            User user2 = initUser("test4@test.com", "test4", region2);

            Team team1 = initTeam("testTeam1", leader1, region1);
            Team team2 = initTeam("testTeam2", leader2, region2);

            TeamApplication savedTeamApplication1 = teamApplicationRepository.save(initTeamApplication(user1, team1));
            TeamApplication savedTeamApplication2 = teamApplicationRepository.save(initTeamApplication(user1, team2));
            TeamApplication savedTeamApplication3 = teamApplicationRepository.save(initTeamApplication(user2, team1));
            TeamApplication savedTeamApplication4 = teamApplicationRepository.save(initTeamApplication(user2, team2));

            Pageable pageable = PageRequest.of(1, 2);

            // when
            Page<TeamApplication> teamApplications = teamApplicationRepository.findAll(pageable);

            // then
            assertThat(teamApplications.getTotalPages()).isEqualTo(2);
            assertThat(teamApplications.getTotalElements()).isEqualTo(4);
            assertThat(teamApplications.getContent().size()).isEqualTo(2);

            assertThat(teamApplications.get().toList().get(0)).isEqualTo(savedTeamApplication3);
        }

        @DisplayName("없는 TeamApplication이면 에러가 발생한다")
        @Test
        public void selectTeamApplication3() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);
            Team team = initTeam("testTeam", leader, region);
            User user = initUser("test2@test.com", "test2", region);

            TeamApplication savedTeamApplication = teamApplicationRepository.save(initTeamApplication(user, team));

            // when, then
            assertThatThrownBy(() -> teamApplicationRepository.findById(savedTeamApplication.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_TEAM_APPLICATION_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 가입 신청이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("UpdateTeamApplicationTest")
    public class UpdateTeamApplicationTest {

        @DisplayName("TeamApplication를 수정한다")
        @Test
        public void updateTeamApplication() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);
            Team team = initTeam("testTeam", leader, region);
            User user = initUser("test2@test.com", "test2", region);

            TeamApplication savedTeamApplication = teamApplicationRepository.save(initTeamApplication(user, team));

            // when
            ReflectionTestUtils.setField(savedTeamApplication, "status", JoinStatus.APPROVE);

            TeamApplication updatedTeamApplication = teamApplicationRepository.findById(savedTeamApplication.getId()).get();

            // then
            assertThat(updatedTeamApplication.getUser()).isEqualTo(user);
            assertThat(updatedTeamApplication.getStatus()).isEqualTo(JoinStatus.APPROVE);
        }
    }

    @Nested
    @DisplayName("DeleteTeamApplicationTest")
    public class DeleteTeamApplicationTest {

        @DisplayName("TeamApplication을 삭제한다")
        @Test
        public void deleteTeamApplication() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);
            Team team = initTeam("testTeam", leader, region);
            User user = initUser("test2@test.com", "test2", region);

            TeamApplication savedTeamApplication = teamApplicationRepository.save(initTeamApplication(user, team));

            // when
            teamRepository.deleteById(savedTeamApplication.getId());

            // then
            assertThat(teamRepository.findById(savedTeamApplication.getId()).isPresent()).isFalse();
        }
    }
}
