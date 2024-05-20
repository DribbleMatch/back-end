package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamApplicationResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamMemberResponseDto;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.JoinStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Import(QuerydslConfig.class)
public class TeamServiceIntegrationTest {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private TeamApplicationRepository teamApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamServiceImpl teamService;

    @AfterEach
    void tearDown() {
        teamApplicationRepository.deleteAllInBatch();
        teamMemberRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        regionRepository.deleteAllInBatch();
    }

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

    private TeamApplication initTeamApplication(User user, Team team) {
        return teamApplicationRepository.save(TeamApplication.builder()
                .user(user)
                .team(team)
                .build());
    }


    // 팀원 관리
    @Nested
    @DisplayName("Join Test")
    public class JoinTest {

        @DisplayName("팀원 가입을 신청한다")
        @Test
        public void joinTeam() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            TeamJoinRequestDto request = TeamJoinRequestDto.builder()
                    .introduce("가난한 대학생")
                    .build();

            // when
            teamService.join(request, team.getId(), member.getId());

            // then
            List<TeamApplication> teamApplications = teamApplicationRepository.findAll();
            assertThat(teamApplications.size()).isEqualTo(1);
            assertThat(teamApplications.get(0).getTeam().getId()).isEqualTo(team.getId());
            assertThat(teamApplications.get(0).getUser().getId()).isEqualTo(member.getId());
        }

        @DisplayName("이미 가입된 팀원이 신청하면 예외가 발생한다")
        @Test
        public void joinTeamAlreadyUser() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);


            teamMemberRepository.save(TeamMember.builder()
                    .team(team)
                    .user(member)
                    .build());

            TeamJoinRequestDto request = TeamJoinRequestDto.builder()
                    .introduce("가난한 대학생")
                    .build();

            // when //then
            assertThatThrownBy(() -> teamService.join(request, team.getId(), member.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 등록된 멤버입니다");
        }

    }

    @Nested
    @DisplayName("FindApplication Test")
    public class FindApplicationTest {

        @DisplayName("팀 가입 신청 내역 조회할 수 있다.")
        @Test
        public void findApplication() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            Pageable pageable = PageRequest.of(0, 2);
            Page<TeamApplicationResponseDto> response =teamService.findApplication(pageable, team.getId());

            // then
            assertThat(response.getSize()).isEqualTo(2);
        }

    }

    @Nested
    @DisplayName("Approve Test")
    public class ApproveTest {

        @DisplayName("팀원 가입 신청을 승인한다.")
        @Test
        public void Approve() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            teamService.approve(teamApplication.getId(), leader.getId());

            // then
            List<TeamMember> teamMembers = teamMemberRepository.findAll();
            assertThat(teamMembers.size()).isEqualTo(2);
            assertThat(teamMembers.get(1).getTeam().getId()).isEqualTo(team.getId());
            assertThat(teamMembers.get(1).getUser().getId()).isEqualTo(member.getId());
        }

        @DisplayName("이미 가입된 팀원은 팀을 다시 가입할 수 없다")
        @Test
        public void approveAlreadyUser() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            initTeamMember(member,team,TeamRole.ADMIN);

            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            assertThatThrownBy(() -> teamService.approve(teamApplication.getId(), leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 등록된 멤버입니다");
        }

        @DisplayName("ADMIN이 아니면 가입요청을 승인 못한다.")
        @Test
        public void approveNotAdmin() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.MEMBER);

            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            assertThatThrownBy(() -> teamService.approve(teamApplication.getId(), leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }


    }

    @Nested
    @DisplayName("Refuse Test")
    public class ApplicationCancelTest {

        @DisplayName("팀원 가입 신청을 취소한.")
        @Test
        public void cancel() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            teamService.cancel(teamApplication.getId(), member.getId());

            // then
            List<TeamApplication> teamApplications = teamApplicationRepository.findAll();
            assertThat(teamApplications.size()).isEqualTo(0);

        }

        @DisplayName("자신이 아니면 팀 가입 신청을 취소할 수 없다")
        @Test
        public void cantCancelNotMe() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            initTeamMember(member,team,TeamRole.ADMIN);

            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            assertThatThrownBy(() -> teamService.cancel(teamApplication.getId(), leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }

    }

    @Nested
    @DisplayName("Refuse Test")
    public class RefuseTest {

        @DisplayName("팀원 가입 신청을 거절한다.")
        @Test
        public void refuse() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            teamService.refuse(teamApplication.getId(), leader.getId());

            // then
            List<TeamApplication> teamApplications = teamApplicationRepository.findAll();
            assertThat(teamApplications.size()).isEqualTo(1);
            assertThat(teamApplications.get(0).getTeam().getId()).isEqualTo(team.getId());
            assertThat(teamApplications.get(0).getUser().getId()).isEqualTo(member.getId());
            assertThat(teamApplications.get(0).getStatus()).isEqualTo(JoinStatus.REFUSE);

        }

        @DisplayName("이미 존재하는 멤버를 거절 시 예외가 발생한다.")
        @Test
        public void refuseAlreadyMember() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            initTeamMember(member,team,TeamRole.ADMIN);

            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            assertThatThrownBy(() -> teamService.approve(teamApplication.getId(), member.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 등록된 멤버입니다");
        }

        @DisplayName("ADMIN권한이 없으면 거절 시 예외가 발생한다.")
        @Test
        public void refuseNoAdmin() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            User member = initUser("user1@test.com","user",region);
            User member2 = initUser("user2@test.com","user2",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            initTeamMember(member2,team,TeamRole.MEMBER);

            TeamApplication teamApplication = initTeamApplication(member,team);

            // when
            assertThatThrownBy(() -> teamService.approve(teamApplication.getId(), member2.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }

    }

    @Nested
    @DisplayName("FindTeamMember Test")
    public class FindTeamMember {

        @DisplayName("팀원들을 조회할 수 있다.")
        @Test
        public void findTeamMember() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test12@test.com","test12", region);
            Team team = initTeam("testTeam12", leader, region);

            User member = initUser("user1@test.com","user",region);

            initTeamMember(leader,team,TeamRole.ADMIN);
            initTeamMember(member,team,TeamRole.MEMBER);

            // when
            List<TeamMemberResponseDto> teamMembers = teamService.findMember(team.getId());

            // then
            assertThat(teamMembers.size()).isEqualTo(2);
            assertThat(teamMembers.get(0).getMemberId()).isEqualTo(leader.getId());
            assertThat(teamMembers.get(0).getNickname()).isEqualTo(leader.getNickName());
            assertThat(teamMembers.get(0).getRole()).isEqualTo(TeamRole.ADMIN);
            assertThat(teamMembers.get(1).getMemberId()).isEqualTo(member.getId());
            assertThat(teamMembers.get(1).getNickname()).isEqualTo(member.getNickName());
            assertThat(teamMembers.get(1).getRole()).isEqualTo(TeamRole.MEMBER);
        }


    }

}
