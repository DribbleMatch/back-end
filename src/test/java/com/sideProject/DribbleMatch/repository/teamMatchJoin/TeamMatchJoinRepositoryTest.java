//package com.sideProject.DribbleMatch.repository.teamMatchJoin;
//
//import com.sideProject.DribbleMatch.common.error.CustomException;
//import com.sideProject.DribbleMatch.common.error.ErrorCode;
//import com.sideProject.DribbleMatch.config.QuerydslConfig;
//import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
//import com.sideProject.DribbleMatch.entity.matching.TeamMatching;
//import com.sideProject.DribbleMatch.entity.region.Region;
//import com.sideProject.DribbleMatch.repository.matching.teamMatching.TeamMatchingRepository;
//import com.sideProject.DribbleMatch.entity.team.Team;
//import com.sideProject.DribbleMatch.repository.region.RegionRepository;
//import com.sideProject.DribbleMatch.repository.team.TeamRepository;
//import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
//import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
//import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
//import com.sideProject.DribbleMatch.entity.user.User;
//import com.sideProject.DribbleMatch.repository.user.UserRepository;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//@Import(QuerydslConfig.class)
//public class TeamMatchJoinRepositoryTest {
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TeamMatchingRepository teamMatchingRepository;
//
//    @Autowired
//    private TeamMatchJoinRepository teamMatchJoinRepository;
//
//    private Region initRegion(String dong) {
//        return regionRepository.save(Region.builder()
//                .siDo("서울특별시")
//                .siGunGu("영등포구")
//                .eupMyeonDongGu(dong)
//                .latitude(37.5347)
//                .longitude(126.9065)
//                .build());
//    }
//
//    private User initUser(String email, String name, Region region) {
//        return userRepository.save(User.builder()
//                .email(email)
//                .password("test1234!A")
//                .nickName(name)
//                .gender(Gender.MALE)
//                .birth(LocalDate.of(2001, 1, 1))
//                .position(Position.CENTER)
//                .winning(10)
//                .region(region)
//                .build());
//    }
//
//    private Team initTeam(String name, User leader, Region region) {
//        return teamRepository.save(Team.builder()
//                .name(name)
//                .winning(10)
//                .leader(leader)
//                .region(region)
//                .info("test")
//                .build());
//    }
//
//    private TeamMatching initTeamMatching(String name, Region region) {
//        return teamMatchingRepository.save(TeamMatching.builder()
//                .name(name)
//                .playPeople(5)
//                .maxPeople(7)
//                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
//                .status(MatchingStatus.RECRUITING)
//                .region(region)
//                .build());
//    }
//
//    private TeamMatchJoin initTeamMatchJoin(Team team, TeamMatching teamMatching) {
//        return TeamMatchJoin.builder()
//                .team(team)
//                .teamMatching(teamMatching)
//                .build();
//    }
//
//    @Nested
//    @DisplayName("CreateTeamMatchJoinTest")
//    public class CreateTeamMatchJoinTest {
//
//        @DisplayName("TeamMatchJoin를 생성한다")
//        @Test
//        public void createTeamMatchJoin() {
//
//            // given
//            Region region = initRegion("당산동");
//            User leader = initUser("test@test.com", "test", region);
//            Team team = initTeam("testTeam", leader, region);
//            TeamMatching teamMatching = initTeamMatching("testTeamMatching", region);
//
//            TeamMatchJoin teamMatchJoin = initTeamMatchJoin(team, teamMatching);
//
//            // when
//            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(teamMatchJoin);
//
//            // then
//            assertThat(savedTeamMatchJoin).isNotNull();
//            assertThat(savedTeamMatchJoin).isEqualTo(teamMatchJoin);
//        }
//
//        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
//        @Test
//        public void createTeamMatchJoin2() {
//
//            // given
//            Region region = initRegion("당산동");
//            User leader = initUser("test@test.com", "test", region);
//            Team team = initTeam("testTeam", leader, region);
//            TeamMatching teamMatching = initTeamMatching("testTeamMatching", region);
//
//            TeamMatchJoin teamMatchJoin = TeamMatchJoin.builder()
//                    .team(team)
////                    .teamMatching(teamMatching)
//                    .build();
//
//            // when, then
//            assertThatThrownBy(() -> teamMatchJoinRepository.save(teamMatchJoin))
//                    .isInstanceOf(ConstraintViolationException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("SelectTeamMatchJoinTest")
//    public class SelectTeamMatchJoinTest {
//
//        @DisplayName("TeamMatchJoin을 조회한다")
//        @Test
//        public void selectTeamMatchJoin() {
//
//            // given
//            Region region = initRegion("당산동");
//            User leader = initUser("test@test.com", "test", region);
//            Team team = initTeam("testTeam", leader, region);
//            TeamMatching teamMatching = initTeamMatching("testTeamMatching", region);
//
//            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(initTeamMatchJoin(team, teamMatching));
//
//            // when
//            TeamMatchJoin selectedTeamMatchJoin = teamMatchJoinRepository.findById(savedTeamMatchJoin.getId()).get();
//
//            // then
//            assertThat(selectedTeamMatchJoin).isNotNull();
//            assertThat(selectedTeamMatchJoin).isEqualTo(savedTeamMatchJoin);
//        }
//
//        @DisplayName("모든 TeamMatchJoin을 조회한다")
//        @Test
//        public void selectTeamMatchJoin2() {
//
//            // given
//            Region region = initRegion("당산동");
//            User leader = initUser("test@test.com", "test", region);
//            Team team = initTeam("testTeam", leader, region);
//            TeamMatching teamMatching = initTeamMatching("testTeamMatching", region);
//
//            TeamMatchJoin savedTeamMatchJoin1 = teamMatchJoinRepository.save(initTeamMatchJoin(team, teamMatching));
//            TeamMatchJoin savedTeamMatchJoin2 = teamMatchJoinRepository.save(initTeamMatchJoin(team, teamMatching));
//
//            // when
//            List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findAll();
//
//            // then
//            assertThat(teamMatchJoins.size()).isEqualTo(2);
//            assertThat(teamMatchJoins.contains(savedTeamMatchJoin1)).isTrue();
//            assertThat(teamMatchJoins.contains(savedTeamMatchJoin2)).isTrue();
//        }
//
//        @DisplayName("없는 TeamMatchJoin이면 에러가 발생한다")
//        @Test
//        public void selectTeamMatchJoin3() {
//
//            // given
//            Region region = initRegion("당산동");
//            User leader = initUser("test@test.com", "test", region);
//            Team team = initTeam("testTeam", leader, region);
//            TeamMatching teamMatching = initTeamMatching("testTeamMatching", region);
//
//            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(initTeamMatchJoin(team, teamMatching));
//
//            // when, then
//            assertThatThrownBy(() -> teamMatchJoinRepository.findById(savedTeamMatchJoin.getId() + 1).orElseThrow(() ->
//                    new CustomException(ErrorCode.NOT_FOUND_TEAM_MATCH_JOIN_ID)))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("해당 팀 경기 참가 정보가 존재하지 않습니다.");
//        }
//    }
//
//    @Nested
//    @DisplayName("DeleteTeamMatchJoinTest")
//    public class DeleteTeamMatchJoinTest {
//
//        @DisplayName("TeamMatchJoin를 삭제한다")
//        @Test
//        public void deleteTeamMatchJoin() {
//
//            // given
//            Region region = initRegion("당산동");
//            User leader = initUser("test@test.com", "test", region);
//            Team team = initTeam("testTeam", leader, region);
//            TeamMatching teamMatching = initTeamMatching("testTeamMatching", region);
//
//            TeamMatchJoin savedTeamMatchJoin = teamMatchJoinRepository.save(initTeamMatchJoin(team, teamMatching));
//
//            // when
//            teamMatchJoinRepository.deleteById(savedTeamMatchJoin.getId());
//
//            // then
//            assertThat(teamMatchJoinRepository.findById(savedTeamMatchJoin.getId()).isPresent()).isFalse();
//        }
//    }
//}
