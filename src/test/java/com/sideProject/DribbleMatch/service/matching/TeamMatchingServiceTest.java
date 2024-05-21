package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.TeamMatchingResponseDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.matching.QTeamMatch.teamMatch;
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

    private TeamMatch initTeamMatch(String name, Team team, LocalDateTime start, Region region, int maxTeam) {
        return teamMatchingRepository.save(TeamMatch.builder()
                .name(name)
                .playPeople(5)
                .maxPeople(12)
                .startAt(start)
                .endAt(start.plusHours(1))
                .region(region)
                .homeTeam(team)
                .status(MatchingStatus.RECRUITING)
                .maxTeam(maxTeam)
                .build());
    }

    private TeamMatchJoin initTeamMatchJoin(TeamMatch teamMatch, Team team) {
        return teamMatchJoinRepository.save(
                TeamMatchJoin.builder()
                        .teamMatch(teamMatch)
                        .team(team)
                        .build()
        );
    }


    @Nested
    @DisplayName("createMatching Test")
    class CreateMatching {

        @DisplayName("팀 리더는 매치를 수정할 수 있다.")
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

        @DisplayName("팀 리더면 매치를 수정할 수 있다.")
        @Test
        void createMatchingNotTeamMember(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);

            TeamMatchingUpdateRequestDto request = new TeamMatchingUpdateRequestDto(
                    "서울 팀 매치 수정",
                    11,
                    16,
                    now.plusDays(3),
                    now.plusDays(3).plusHours(2),
                    "서울시 영등포구 가야동"
            );

            teamMatchingService.updateMatching(request,match.getId(), leader.getId());

            //when //then
            TeamMatch teamMatches = teamMatchingRepository.findById(team.getId()).orElseThrow();
            assertThat(teamMatches.getName()).isEqualTo(request.getName());
            assertThat(teamMatches.getMaxTeam()).isEqualTo(request.getMaxTeam());
            assertThat(teamMatches.getStartAt()).isEqualTo(request.getStartAt());
            assertThat(teamMatches.getEndAt()).isEqualTo(request.getEndAt());

        }

        @DisplayName("팀 리더가 아니면 매치를 수정할 수 없다.")
        @Test
        void updateMatchingNotTeamLeader(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.MEMBER);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);

            TeamMatchingUpdateRequestDto request = new TeamMatchingUpdateRequestDto(
                    "서울 팀 매치 수정",
                    11,
                    16,
                    now.plusDays(3),
                    now.plusDays(3).plusHours(2),
                    "서울시 영등포구 가야동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.updateMatching(request,match.getId(), leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }

        @DisplayName("팀이 멤버가 아니면 매치를 수정할 수 없다.")
        @Test
        void updateMatchingNotTeam(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);

            TeamMatchingUpdateRequestDto request = new TeamMatchingUpdateRequestDto(
                    "서울 팀 매치 수정",
                    11,
                    16,
                    now.plusDays(3),
                    now.plusDays(3).plusHours(2),
                    "서울시 영등포구 가야동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.updateMatching(request,match.getId(), leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀원이 존재하지 않습니다.");
        }

        @DisplayName("매치가 없으면 매치를 수정할 수 없다.")
        @Test
        void updateMatchingNotMatch(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            LocalDateTime now = LocalDateTime.now();

            TeamMatchingUpdateRequestDto request = new TeamMatchingUpdateRequestDto(
                    "서울 팀 매치 수정",
                    11,
                    16,
                    now.plusDays(3),
                    now.plusDays(3).plusHours(2),
                    "서울시 영등포구 가야동"
            );

            //when //then
            assertThatThrownBy(() -> teamMatchingService.updateMatching(request,1L, leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀 경기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteMatching Test")
    class DeleteMatch {

        @DisplayName("매치를 삭제할 수 있다.")
        @Test
        void deleteMatch(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            //when
            Long id = teamMatchingService.deleteMatching(match.getId(),leader.getId());

            //then
            List<TeamMatch> teamMatches = teamMatchingRepository.findAll();
            List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findAll();
            assertThat(teamMatches.size()).isEqualTo(0);
            assertThat(teamMatchJoins.size()).isEqualTo(0);
            assertThat(id).isEqualTo(match.getId());
        }

        @DisplayName("리더가 아니면 매치를 삭제할 수 없다")
        @Test
        void deleteMatchNotLeader(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            initTeamMember(leader, team,TeamRole.MEMBER);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            //when

            //then
            assertThatThrownBy(() -> teamMatchingService.deleteMatching(match.getId(),leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }

        @DisplayName("팀원이 아니면 매치를 삭제할 수 없다")
        @Test
        void deleteMatchNotTeamMember(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            //when

            //then
            assertThatThrownBy(() -> teamMatchingService.deleteMatching(match.getId(),leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀원이 존재하지 않습니다.");
        }

        @DisplayName("없는 매치는 삭제할 수 없다")
        @Test
        void deleteMatchNotMatch(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region seoul2 = initRegion("서울시","가야동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);


            //when

            //then
            assertThatThrownBy(() -> teamMatchingService.deleteMatching(1L,leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀 경기가 존재하지 않습니다.");
        }


    }

    @Nested
    @DisplayName("joinMatching Test")
    class JoinMatchingTest {
        @DisplayName("매치에 가입할 수 있다.")
        @Test
        void joinMatchingTest(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User team2Leader = initUser("test13@test.com", "test3", seoul);
            Team team2 = initTeam("testTeam2", team2Leader, seoul);

            initTeamMember(team2Leader, team2,TeamRole.ADMIN);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            //when
            String result = teamMatchingService.joinMatching(
                    match.getId(),
                    team2.getId(),
                    team2Leader.getId()
            );

            //then
            assertThat(result).isEqualTo("참가 신청이 완료되었습니다.");
            List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findAll();
            assertThat(teamMatchJoins.size()).isEqualTo(2);
        }

        @DisplayName("매치 모집이 마감되면 상태가 Close로 변경된다.")
        @Test
        void joinMatchingTestMaxTeam(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User team2Leader = initUser("test13@test.com", "test3", seoul);
            Team team2 = initTeam("testTeam2", team2Leader, seoul);

            initTeamMember(team2Leader, team2,TeamRole.ADMIN);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            //when
            String result = teamMatchingService.joinMatching(
                    match.getId(),
                    team2.getId(),
                    team2Leader.getId()
            );

            //then
            assertThat(result).isEqualTo("참가 신청이 완료되었습니다.");
            TeamMatch teamMatch = teamMatchingRepository.findById(match.getId()).orElseThrow();
            List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findAll();
            assertThat(teamMatchJoins.size()).isEqualTo(2);
            assertThat(teamMatch.getStatus()).isEqualTo(MatchingStatus.CLOSED);
        }

        @DisplayName("팀리더가 아니면 매치를 가입할 수 없다.")
        @Test
        void joinMatchingNotTeamLeader(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User team2Leader = initUser("test13@test.com", "test3", seoul);
            Team team2 = initTeam("testTeam2", team2Leader, seoul);

            initTeamMember(team2Leader, team2,TeamRole.MEMBER);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            //when
            String result = teamMatchingService.joinMatching(
                    match.getId(),
                    team2.getId(),
                    team2Leader.getId()
            );

            //then
            assertThatThrownBy(() ->  teamMatchingService.joinMatching(
                    match.getId(),
                    team2.getId(),
                    team2Leader.getId()
            ))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }

        @DisplayName("마감된 매치에 가입할 수 없다.")
        @Test
        void joinMatchingClosed(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User team2Leader = initUser("test13@test.com", "test3", seoul);
            Team team2 = initTeam("testTeam2", team2Leader, seoul);

            initTeamMember(team2Leader, team2,TeamRole.ADMIN);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            match.close();
            teamMatchingRepository.save(match);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            //when
            //then
            assertThatThrownBy(() ->  teamMatchingService.joinMatching(
                    match.getId(),
                    team2.getId(),
                    team2Leader.getId()
            ))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("모집 마감된 경기입니다.");
        }

        @DisplayName("모집 팀 수가 가득찬 매치에 가입할 수 없다")
        @Test
        void joinMatchingMaxTeam(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User team2Leader = initUser("test13@test.com", "test3", seoul);
            Team team2 = initTeam("testTeam2", team2Leader, seoul);

            initTeamMember(team2Leader, team2,TeamRole.ADMIN);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,2);
            match.close();
            TeamMatchJoin join = initTeamMatchJoin(match, team);
            TeamMatchJoin join2 = initTeamMatchJoin(match, team2);

            //when
            //then
            assertThatThrownBy(() ->  teamMatchingService.joinMatching(
                    match.getId(),
                    team2.getId(),
                    team2Leader.getId()
            ))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("모집 마감된 경기입니다.");
        }

        @DisplayName("모집 팀 수가 가득찬 매치에 가입할 수 없다")
        @Test
        void joinMatchingAlReady(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);
            User team2Leader = initUser("test13@test.com", "test3", seoul);
            Team team2 = initTeam("testTeam2", team2Leader, seoul);

            initTeamMember(team2Leader, team2,TeamRole.ADMIN);
            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,3);
            match.close();
            TeamMatchJoin join = initTeamMatchJoin(match, team);
            TeamMatchJoin join2 = initTeamMatchJoin(match, team2);

            //when
            //then
            assertThatThrownBy(() ->  teamMatchingService.joinMatching(
                    match.getId(),
                    team2.getId(),
                    team2Leader.getId()
            ))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 참가된 팀입니다.");
        }
    }

    @Nested
    @DisplayName("findMatching Test")
    class FindMatchingTest {
        @DisplayName("id로 매치를 조회할 수 있다.")
        @Test
        void findMatching(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);

            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,3);
            TeamMatchJoin join = initTeamMatchJoin(match, team);
            //when
            TeamMatchingResponseDto matching = teamMatchingService.findMatching(match.getId());

            //then
            assertThat(matching.getName()).isEqualTo(match.getName());
            assertThat(matching.getStartAt()).isEqualTo(match.getStartAt());
        }

        @DisplayName("없는 매치는 조회할 수 없다.")
        @Test
        void findMatchingNotMatch(){
            //given
            //when

            //then
            assertThatThrownBy(() ->  teamMatchingService.findMatching(1L))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀 경기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("findMatching(Page) Test")
    class FindMatchingPageTest {
        @DisplayName("매치 페이지를 조회 가능하다")
        @Test
        void findMatchingPage(){
            //given
            Region seoul = initRegion("서울시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);

            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치", team,now.plusDays(2), seoul,3);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            TeamMatch match2 = initTeamMatch("서울 팀 매치", team,now.plusDays(3), seoul,3);
            TeamMatchJoin join1 = initTeamMatchJoin(match, team);
            //when
            Pageable pageable = PageRequest.of(0, 5);
            Page<TeamMatchingResponseDto> matching = teamMatchingService.findMatching(pageable, null, now);

            //then
            assertThat(matching.getContent().size()).isEqualTo(2);
        }

        @DisplayName("도시 이름으로매치 페이지를 조회 가능하다")
        @Test
        void findMatchingPageBySido(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region ulsan = initRegion("울산시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);

            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치1", team,now.plusDays(2), seoul,3);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            TeamMatch match2 = initTeamMatch("울산 팀 매치2", team,now.plusDays(3), ulsan,3);
            TeamMatchJoin join1 = initTeamMatchJoin(match2, team);

            TeamMatch match3 = initTeamMatch("서울 팀 매치3", team,now.plusDays(4), seoul,3);
            TeamMatchJoin join3 = initTeamMatchJoin(match3, team);
            //when
            Pageable pageable = PageRequest.of(0, 5);
            Page<TeamMatchingResponseDto> matching = teamMatchingService.findMatching(pageable, "울산시", now);

            //then
            assertThat(matching.getContent().size()).isEqualTo(1);
        }

        @DisplayName("오늘 이후의 매치들만 조회 가능하다")
        @Test
        void findMatchingPageAfterNow(){
            //given
            Region seoul = initRegion("서울시","당산동");
            Region ulsan = initRegion("울산시","당산동");
            User leader = initUser("test12@test.com", "test1", seoul);
            Team team = initTeam("testTeam1", leader, seoul);

            initTeamMember(leader, team,TeamRole.ADMIN);
            LocalDateTime now = LocalDateTime.now();

            TeamMatch match = initTeamMatch("서울 팀 매치1", team,now.minusDays(3), seoul,3);
            TeamMatchJoin join = initTeamMatchJoin(match, team);

            TeamMatch match2 = initTeamMatch("울산 팀 매치2", team,now.plusDays(3), ulsan,3);
            TeamMatchJoin join1 = initTeamMatchJoin(match2, team);

            TeamMatch match3 = initTeamMatch("서울 팀 매치3", team,now.plusDays(4), seoul,3);
            TeamMatchJoin join3 = initTeamMatchJoin(match3, team);
            //when
            Pageable pageable = PageRequest.of(0, 5);
            Page<TeamMatchingResponseDto> matching = teamMatchingService.findMatching(pageable, "서울시", now);

            //then
            assertThat(matching.getContent().size()).isEqualTo(1);
        }
    }


}
