package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.dto.matching.response.TeamMatchingResponseDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.recruitment.RecruitmentRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TeamMatchCustomRepositoryTest {
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

    @AfterEach
    void tearDown() {
        teamMatchingRepository.deleteAllInBatch();
        recruitmentRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
    }

    private Region initRegion(String sido,String dong) {
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
    @DisplayName("FindTeamMatch Test")
    public class FindTeamMatch {

        @DisplayName("TeamMatch를 조회할 수 있다.")
        @Test
        public void findTeamMatch() {

            // given
            Region seoul = initRegion("서울시","당산동");
            Region ulsan = initRegion("울산","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User leader1 = initUser("test123@test.com", "test2", ulsan);
            Team team1 = initTeam("testTeam2", leader1, ulsan);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch teamMatch1 = initTeamMatch("1일 대전",team,now.plusHours(5),seoul);
            TeamMatch teamMatch2 = initTeamMatch("2일 대전",team,now.plusDays(5),seoul);
            TeamMatch teamMatch3 = initTeamMatch("3일 대전",team1,now.plusDays(5),ulsan);

            // when
            Pageable pageable = PageRequest.of(0, 5);
            Page<TeamMatch> teamMatches = teamMatchingRepository.find(pageable, null);

            // then
            assertThat(teamMatches.getContent().size()).isEqualTo(3);
            assertThat(teamMatches.getContent().get(0).getName()).isEqualTo(teamMatch1.getName());
            assertThat(teamMatches.getContent().get(0).getHomeTeam().getId()).isEqualTo(teamMatch1.getHomeTeam().getId());
            assertThat(teamMatches.getContent().get(1).getName()).isEqualTo(teamMatch2.getName());
            assertThat(teamMatches.getContent().get(1).getHomeTeam().getId()).isEqualTo(teamMatch2.getHomeTeam().getId());
            assertThat(teamMatches.getContent().get(2).getName()).isEqualTo(teamMatch3.getName());
            assertThat(teamMatches.getContent().get(2).getHomeTeam().getId()).isEqualTo(teamMatch3.getHomeTeam().getId());
        }

        @DisplayName("TeamMatch를 시도로 조회할 수 있다.")
        @Test
        public void findTeamMatchByName() {

            // given
            Region seoul = initRegion("서울시","당산동");
            Region ulsan = initRegion("울산","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User leader1 = initUser("test123@test.com", "test2", ulsan);
            Team team1 = initTeam("testTeam2", leader1, ulsan);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch teamMatch1 = initTeamMatch("1일 대전",team,now.plusHours(5),seoul);
            TeamMatch teamMatch2 = initTeamMatch("2일 대전",team,now.plusDays(5),seoul);
            TeamMatch teamMatch3 = initTeamMatch("3일 대전",team1,now.plusDays(5),ulsan);

            // when
            Pageable pageable = PageRequest.of(0, 5);
            Page<TeamMatch> teamMatches = teamMatchingRepository.find(pageable, "서울시");

            // then
            assertThat(teamMatches.getContent().size()).isEqualTo(2);
            assertThat(teamMatches.getContent().get(0).getName()).isEqualTo(teamMatch1.getName());
            assertThat(teamMatches.getContent().get(0).getHomeTeam().getId()).isEqualTo(teamMatch1.getHomeTeam().getId());
            assertThat(teamMatches.getContent().get(1).getName()).isEqualTo(teamMatch2.getName());
            assertThat(teamMatches.getContent().get(1).getHomeTeam().getId()).isEqualTo(teamMatch2.getHomeTeam().getId());
        }

        @DisplayName("TeamMatch를 지역명이 울산인 매치들을 조회할 수 있다.")
        @Test
        public void findTeamMatchByRegionName_울산() {

            // given
            Region seoul = initRegion("서울시","당산동");
            Region ulsan = initRegion("울산","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User leader1 = initUser("test123@test.com", "test2", ulsan);
            Team team1 = initTeam("testTeam2", leader1, ulsan);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch teamMatch1 = initTeamMatch("1일 대전",team,now.plusHours(5),seoul);
            TeamMatch teamMatch2 = initTeamMatch("2일 대전",team,now.plusDays(5),seoul);
            TeamMatch teamMatch3 = initTeamMatch("3일 대전",team1,now.plusDays(5),ulsan);

            // when
            Pageable pageable = PageRequest.of(0, 5);
            Page<TeamMatch> teamMatches = teamMatchingRepository.find(pageable, "울산");

            // then
            assertThat(teamMatches.getContent().size()).isEqualTo(1);
            assertThat(teamMatches.getContent().get(0).getName()).isEqualTo(teamMatch3.getName());
            assertThat(teamMatches.getContent().get(0).getHomeTeam().getId()).isEqualTo(teamMatch3.getHomeTeam().getId());
        }


        @DisplayName("TeamMatch를 조회할 때 조건을 만족하는 데이터가 없으면 빈 값이 조회된다")
        @Test
        public void findTeamMatchByRegionNameNot() {

            // given
            Region seoul = initRegion("서울시","당산동");
            Region ulsan = initRegion("울산","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User leader1 = initUser("test123@test.com", "test2", ulsan);
            Team team1 = initTeam("testTeam2", leader1, ulsan);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch teamMatch1 = initTeamMatch("1일 대전",team,now.plusHours(5),seoul);
            TeamMatch teamMatch2 = initTeamMatch("2일 대전",team,now.plusDays(5),seoul);
            TeamMatch teamMatch3 = initTeamMatch("3일 대전",team1,now.plusDays(5),ulsan);

            // when
            Pageable pageable = PageRequest.of(0, 5);
            Page<TeamMatch> teamMatches = teamMatchingRepository.find(pageable, "대전");

            // then
            assertThat(teamMatches.getContent().size()).isEqualTo(0);
        }
    }
}