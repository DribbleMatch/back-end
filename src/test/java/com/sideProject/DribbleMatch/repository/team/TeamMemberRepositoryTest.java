package com.sideProject.DribbleMatch.repository.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
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
public class TeamMemberRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

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
                .maxNumber(10)
                .info("testInfo")
                .leader(leader)
                .region(region)
                .build());
    }

    private TeamMember initTeamMember(User user, Team team) {
        return TeamMember.builder()
                .user(user)
                .team(team)
                .build();
    }

    @Nested
    @DisplayName("CreateTeamMemberTest")
    public class CreateTeamMemberTest {

        @DisplayName("TeamMember을 생성한다")
        @Test
        public void createTeamMember() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember teamMember = initTeamMember(user, team);

            // when
            TeamMember savedTeamMember = teamMemberRepository.save(teamMember);

            // then
            assertThat(savedTeamMember).isNotNull();
            assertThat(savedTeamMember.getUser()).isEqualTo(user);
            assertThat(savedTeamMember.getTeam()).isEqualTo(team);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createTeamMember2() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember teamMember = TeamMember.builder()
                    .user(user)
//                    .team(team)
                    .build();

            // when, then
            assertThatThrownBy(() -> teamMemberRepository.save(teamMember))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectTeamMemberTest")
    public class SelectTeamMemberTest {

        @DisplayName("TeamMember을 조회한다")
        @Test
        public void selectTeamMember() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember savedTeamMember = teamMemberRepository.save(initTeamMember(user, team));

            // when
            TeamMember selectedTeamMember = teamMemberRepository.findById(savedTeamMember.getId()).get();

            // then
            assertThat(selectedTeamMember).isNotNull();
            assertThat(selectedTeamMember).isEqualTo(savedTeamMember);
        }

        @DisplayName("모든 TeamMember을 조회한다")
        @Test
        public void selectTeamMember2() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember savedTeamMember1 = teamMemberRepository.save(initTeamMember(user, team));
            TeamMember savedTeamMember2 = teamMemberRepository.save(initTeamMember(user, team));

            // when
            List<TeamMember> teamMembers = teamMemberRepository.findAll();

            // then
            assertThat(teamMembers.size()).isEqualTo(2);
            assertThat(teamMembers.contains(savedTeamMember1)).isTrue();
            assertThat(teamMembers.contains(savedTeamMember2)).isTrue();
        }

        @DisplayName("없는 TeamMember이면 에러가 발생한다")
        @Test
        public void selectTeamMember3() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember savedTeamMember = teamMemberRepository.save(initTeamMember(user, team));

            // when, then
            assertThatThrownBy(() -> teamMemberRepository.findById(savedTeamMember.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 소속팀 정보가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteTeamMemberTest")
    public class DeleteTeamMemberTest {

        @DisplayName("TeamMember을 삭제한다")
        @Test
        public void deleteTeamMember() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember savedTeamMember = teamMemberRepository.save(initTeamMember(user, team));

            // when
            teamMemberRepository.deleteById(savedTeamMember.getId());

            // then
            assertThat(teamMemberRepository.findById(savedTeamMember.getId()).isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("FindByUserAndTeamTest")
    public class FindByUserAndTeamTest {

        @DisplayName("User, Team으로 TeamMember을 조회한다")
        @Test
        public void findByUserAndTeam() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test1@test.com", "test1", region);
            User leader = initUser("test2@test.com", "test2", region);
            Team team = initTeam("testTeam", leader, region);

            TeamMember savedTeamMember = teamMemberRepository.save(initTeamMember(user, team));

            // when
            TeamMember selectedTeamMember = teamMemberRepository.findByUserAndTeam(user, team).get();

            // then
            assertThat(selectedTeamMember).isEqualTo(savedTeamMember);
        }
    }
}
