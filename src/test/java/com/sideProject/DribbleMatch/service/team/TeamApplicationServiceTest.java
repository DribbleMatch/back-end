package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamApplicationServiceTest {

    @Mock
    private TeamApplicationRepository teamApplicationRepository;

    @Mock
    private TeamMemberService teamMemberService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamApplicationServiceImpl teamApplicationService;

    private Region initRegion(String dong) {
        return Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build();
    }

    private User initUser(String email, String name, Region region) {
        return User.builder()
                .email(email)
                .password("test1234!A")
                .nickName(name)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .region(region)
                .build();
    }

    private Team initTeam(String name, User leader, Region region) {
        return Team.builder()
                .name(name)
                .winning(10)
                .maxNumber(10)
                .info("testInfo")
                .leader(leader)
                .region(region)
                .build();
    }

    private TeamMember initTeamMember(User user, Team team, TeamRole teamRole) {
        return TeamMember.builder()
                .user(user)
                .team(team)
                .teamRole(teamRole)
                .build();
    }

    private TeamApplication initTeamApplication(User user, Team team) {
        return TeamApplication.builder()
                .user(user)
                .team(team)
                .introduce("testIntroduce")
                .build();
    }

    @Nested
    @DisplayName("ApplyTest")
    public class ApplyTest {

        @DisplayName("팀 가입을 신청한다")
        @Test
        public void apply() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);

            Team team = initTeam("testTeam", leader, region);
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamApplication teamApplication = initTeamApplication(applicant, team);
            Long fakeTeamApplicationId = 3L;
            ReflectionTestUtils.setField(teamApplication, "id", fakeTeamApplicationId);

            TeamJoinRequestDto requestDto = TeamJoinRequestDto.builder()
                    .teamId(fakeTeamId)
                    .introduce("testIntroduce")
                    .build();

            //mocking
            when(userRepository.findById(fakeApplicantId)).thenReturn(Optional.ofNullable(applicant));
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.ofNullable(team));
            doNothing().when(teamMemberService).checkAlreadyMember(applicant, team);
            when(teamApplicationRepository.save(any(TeamApplication.class))).thenReturn(teamApplication);

            // when
            Long teamApplicationId = teamApplicationService.apply(fakeApplicantId, requestDto);

            // then
            assertThat(teamApplicationId).isEqualTo(fakeTeamApplicationId);
        }

        @DisplayName("없는 User이면 에러가 발생한다")
        @Test
        public void apply2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);

            Team team = initTeam("testTeam", leader, region);
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamJoinRequestDto requestDto = TeamJoinRequestDto.builder()
                    .teamId(fakeTeamId)
                    .introduce("testIntroduce")
                    .build();

            //mocking
            when(userRepository.findById(fakeApplicantId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> teamApplicationService.apply(fakeApplicantId, requestDto))
                    .hasMessage("해당 사용자가 존재하지 않습니다.")
                    .isInstanceOf(CustomException.class);
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void apply3() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);

            Team team = initTeam("testTeam", leader, region);
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamJoinRequestDto requestDto = TeamJoinRequestDto.builder()
                    .teamId(fakeTeamId)
                    .introduce("testIntroduce")
                    .build();

            //mocking
            when(userRepository.findById(fakeApplicantId)).thenReturn(Optional.ofNullable(applicant));
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> teamApplicationService.apply(fakeApplicantId, requestDto))
                    .hasMessage("해당 팀이 존재하지 않습니다.")
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    @DisplayName("ApproveTest")
    public class ApproveTest {

        @DisplayName("팀 가입 요청을 승인한다")
        @Test
        public void approve1() {

            // given
            Region region = initRegion("당산동");

            User leader = initUser("test1@test.com", "test1", region);
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader, region);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamApplication teamApplication = initTeamApplication(applicant, team);
            Long fakeTeamApplicationId = 3L;
            ReflectionTestUtils.setField(teamApplication, "id", fakeTeamApplicationId);

            // mocking
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(leader));
            when(teamApplicationRepository.findById(fakeTeamApplicationId)).thenReturn(Optional.ofNullable(teamApplication));
            doNothing().when(teamMemberService).checkRole(any(User.class), any(Team.class));
            when(teamMemberService.join(any(User.class), any(Team.class))).thenReturn(fakeTeamApplicationId);

            // when
            Long teamApplicationId = teamApplicationService.approve(fakeUserId, fakeTeamApplicationId);

            // then
            assertThat(teamApplicationId).isEqualTo(fakeTeamApplicationId);
        }
        @DisplayName("없는 User이면 에러가 발생한다")
        @Test
        public void approve2() {

            // given
            Region region = initRegion("당산동");

            User leader = initUser("test1@test.com", "test1", region);
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader, region);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamApplication teamApplication = initTeamApplication(applicant, team);
            Long fakeTeamApplicationId = 3L;
            ReflectionTestUtils.setField(teamApplication, "id", fakeTeamApplicationId);

            // mocking
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> teamApplicationService.approve(fakeUserId, fakeTeamApplicationId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 사용자가 존재하지 않습니다.");
        }

        @DisplayName("없는 TeamApplication이면 에러가 발생한다")
        @Test
        public void approve3() {

            // given
            Region region = initRegion("당산동");

            User leader = initUser("test1@test.com", "test1", region);
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader, region);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamApplication teamApplication = initTeamApplication(applicant, team);
            Long fakeTeamApplicationId = 3L;
            ReflectionTestUtils.setField(teamApplication, "id", fakeTeamApplicationId);

            // mocking
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(leader));
            when(teamApplicationRepository.findById(fakeTeamApplicationId)).thenReturn(Optional.empty());


            // when
            assertThatThrownBy(() -> teamApplicationService.approve(fakeUserId, fakeTeamApplicationId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 가입 신청이 존재하지 않습니다.");
        }

    }

    @Nested
    @DisplayName("RefuseTest")
    public class RefuseTest {

        @DisplayName("팀 가입 요청을 거절한다")
        @Test
        public void refuse() {

            // given
            Region region = initRegion("당산동");

            User leader = initUser("test1@test.com", "test1", region);
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader, region);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamApplication teamApplication = initTeamApplication(applicant, team);
            Long fakeTeamApplicationId = 3L;
            ReflectionTestUtils.setField(teamApplication, "id", fakeTeamApplicationId);

            // mocking
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(leader));
            when(teamApplicationRepository.findById(fakeTeamApplicationId)).thenReturn(Optional.ofNullable(teamApplication));
            doNothing().when(teamMemberService).checkRole(any(User.class), any(Team.class));

            // when
            Long teamApplicationId = teamApplicationService.refuse(fakeUserId, fakeTeamApplicationId);

            // then
            assertThat(teamApplicationId).isEqualTo(fakeTeamApplicationId);
        }

        @DisplayName("없는 User이면 에러가 발생한다")
        @Test
        public void approve2() {

            // given
            Region region = initRegion("당산동");

            User leader = initUser("test1@test.com", "test1", region);
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader, region);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamApplication teamApplication = initTeamApplication(applicant, team);
            Long fakeTeamApplicationId = 3L;
            ReflectionTestUtils.setField(teamApplication, "id", fakeTeamApplicationId);

            // mocking
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> teamApplicationService.refuse(fakeUserId, fakeTeamApplicationId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 사용자가 존재하지 않습니다.");
        }

        @DisplayName("없는 TeamApplication이면 에러가 발생한다")
        @Test
        public void approve3() {

            // given
            Region region = initRegion("당산동");

            User leader = initUser("test1@test.com", "test1", region);
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader, region);

            User applicant = initUser("test2@test.com", "test2", region);
            Long fakeApplicantId = 2L;
            ReflectionTestUtils.setField(applicant, "id", fakeApplicantId);

            TeamApplication teamApplication = initTeamApplication(applicant, team);
            Long fakeTeamApplicationId = 3L;
            ReflectionTestUtils.setField(teamApplication, "id", fakeTeamApplicationId);

            // mocking
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(leader));
            when(teamApplicationRepository.findById(fakeTeamApplicationId)).thenReturn(Optional.empty());


            // when
            assertThatThrownBy(() -> teamApplicationService.refuse(fakeUserId, fakeTeamApplicationId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 가입 신청이 존재하지 않습니다.");
        }
    }
}
