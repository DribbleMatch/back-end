package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingCreateRequestDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.matching.TeamMatchingRepository;
import com.sideProject.DribbleMatch.repository.recruitment.RecruitmentRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamMatchJoin.TeamMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Import(QuerydslConfig.class)
public class TeamMatchingServiceTest {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;
    @Autowired
    private TeamMatchingRepository teamMatchingRepository;
    @Autowired
    private TeamMatchJoinRepository teamMatchJoinRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private TeamMatchingService teamMatchingService;

    @AfterEach
    void tearDown() {
        teamMatchJoinRepository.deleteAllInBatch();
        teamMatchingRepository.deleteAllInBatch();
        recruitmentRepository.deleteAllInBatch();
        teamMemberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
    }

    private Region initRegion(String sido, String dong) {
        return regionRepository.save(Region.builder()
                .siDo(sido)
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

    private TeamMember initTeamMember(User user, Team team, TeamRole role) {
        TeamMember member = TeamMember.builder()
                .user(user)
                .team(team)
                .build();
        if(role.equals(TeamRole.ADMIN)){
            member.advancement();
        }

        return teamMemberRepository.save(member);
    }

    private Team initTeam(String name, User leader, Region region) {
        return teamRepository.save(Team.builder()
                .name(name)
                .winning(10)
                .leader(leader)
                .region(region)
                .build());
    }

    private TeamMatch initTeamMatch(String name, Team team, LocalDateTime start, Region region) {
        return teamMatchingRepository.save(TeamMatch.builder()
                .name(name)
                .playPeople(5)
                .maxPeople(12)
                .startAt(start)
                .endAt(start.plusHours(1))
                .region(region)
                .homeTeam(team)
                .status(MatchingStatus.RECRUITING)
                .maxTeam(2)
                .build());
    }

    @Nested
    @DisplayName("createMatching Test")
    class CreateMatching {

        @DisplayName("팀 리더는 매치를 생성할 수 있다.")
        @Test
        void createMatching(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when
            teamMatchingService.createMatching(request,team.getId(), leader.getId());

            //then
            List<TeamMatch> teamMatches = teamMatchingRepository.findAll();
            List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findAll();
            assertThat(teamMatches.size()).isEqualTo(1);
            assertThat(teamMatchJoins.size()).isEqualTo(1);
        }

        @DisplayName("팀 리더가 아니면 매치를 생성할 수 없다.")
        @Test
        void createMatchingNotLeader(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.MEMBER);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.createMatching(request,team.getId(), leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }

        @DisplayName("팀이 아니면 매치를 생성할 수 없다.")
        @Test
        void createMatchingNotTeam(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.createMatching(request,2L, leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }

        @DisplayName("팀 멤버가 아니면 매치를 생성할 수 없다.")
        @Test
        void createMatchingNotTeamMember(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.createMatching(request,2L, leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("updateMatching Test")
    class UpdateMatching {

        @DisplayName("팀 리더는 매치를 생성할 수 있다.")
        @Test
        void createMatching(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when
            teamMatchingService.createMatching(request,team.getId(), leader.getId());

            //then
            List<TeamMatch> teamMatches = teamMatchingRepository.findAll();
            List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findAll();
            assertThat(teamMatches.size()).isEqualTo(1);
            assertThat(teamMatchJoins.size()).isEqualTo(1);
        }

        @DisplayName("팀 리더가 아니면 매치를 생성할 수 없다.")
        @Test
        void createMatchingNotLeader(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.MEMBER);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.createMatching(request,team.getId(), leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }

        @DisplayName("팀이 아니면 매치를 생성할 수 없다.")
        @Test
        void createMatchingNotTeam(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.createMatching(request,2L, leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }

        @DisplayName("팀 멤버가 아니면 매치를 생성할 수 없다.")
        @Test
        void createMatchingNotTeamMember(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingCreateRequestDto request = new TeamMatchingCreateRequestDto(
                    team.getId(),
                    "서울 팀 매치",
                    10,
                    15,
                    2,
                    now.plusDays(2),
                    now.plusDays(2).plusHours(2),
                    "서울시 영등포구 당산동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.createMatching(request,2L, leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }
    }
}
