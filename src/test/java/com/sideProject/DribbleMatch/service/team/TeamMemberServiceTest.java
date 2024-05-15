package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
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
public class TeamMemberServiceTest {

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamMemberServiceImpl teamMemberService;

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

    @Nested
    @DisplayName("JoinTest")
    public class JoinTest {

        @DisplayName("팀원을 추가한다")
        @Test
        public void join() {

            // given
            Region region = initRegion("당산동");

            User admin = initUser("test1@test.com", "test1", region);
            Long fakeAdminId = 1L;
            ReflectionTestUtils.setField(admin, "id", fakeAdminId);

            User user = initUser("test2@test.com", "test2", region);
            Long fakeUserId = 2L;
            ReflectionTestUtils.setField(user, "id", fakeUserId);

            Team team = initTeam("testTeam", admin, region);
            Long fakeTeamId = 3L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            TeamMember teamMember = initTeamMember(user, team, TeamRole.MEMBER);
            Long fakeTeamMemberId = 4L;
            ReflectionTestUtils.setField(teamMember, "id", fakeTeamMemberId);

            TeamJoinRequestDto requestDto = TeamJoinRequestDto.builder()
                    .teamId(fakeTeamId)
                    .introduce("testIntroduce")
                    .build();

            // mocking
            when(teamMemberRepository.findByUserAndTeam(user, team)).thenReturn(Optional.empty());
            when(teamMemberRepository.save(any())).thenReturn(teamMember);

            // when
            Long teamMemberId = teamMemberService.join(user, team);

            // then
            assertThat(teamMemberId).isEqualTo(fakeTeamMemberId);
        }
    }

    @Nested
    @DisplayName("WithdrawTest")
    public class WithdrawTest {

        @DisplayName("팀원을 삭제한다")
        @Test
        public void withdraw() {

            // given
            Region region = initRegion("당산동");

            User admin = initUser("test1@test.com", "test1", region);
            Long fakeAdminId = 1L;
            ReflectionTestUtils.setField(admin, "id", fakeAdminId);

            User user = initUser("test2@test.com", "test2", region);
            Long fakeUserId = 2L;
            ReflectionTestUtils.setField(user, "id", fakeUserId);

            Team team = initTeam("testTeam", admin, region);
            Long fakeTeamId = 3L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            TeamMember teamMember1 = initTeamMember(admin, team, TeamRole.ADMIN);
            Long fakeUserTeamMemberId = 1L;
            TeamMember teamMember2 = initTeamMember(user, team, TeamRole.MEMBER);
            ReflectionTestUtils.setField(teamMember2, "id", fakeUserTeamMemberId);

            // mocking
            when(userRepository.findById(fakeAdminId)).thenReturn(Optional.ofNullable(admin));
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(user));
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.ofNullable(team));
            when(teamMemberRepository.findByUserAndTeam(admin, team)).thenReturn(Optional.ofNullable(teamMember1));
            when(teamMemberRepository.findByUserAndTeam(user, team)).thenReturn(Optional.ofNullable(teamMember2));
            doNothing().when(teamMemberRepository).delete(any(TeamMember.class));

            // when
            Long deletedTeamMemberId = teamMemberService.withdraw(fakeAdminId, fakeUserId, fakeTeamId);

            // then
            assertThat(deletedTeamMemberId).isEqualTo(fakeUserTeamMemberId);
        }

        @DisplayName("admin이 없는 User이면 에러가 발생한다")
        @Test
        public void withdraw2() {

            // given
            Region region = initRegion("당산동");

            User admin = initUser("test1@test.com", "test1", region);
            Long fakeAdminId = 1L;
            ReflectionTestUtils.setField(admin, "id", fakeAdminId);

            User user = initUser("test2@test.com", "test2", region);
            Long fakeUserId = 2L;
            ReflectionTestUtils.setField(user, "id", fakeUserId);

            Team team = initTeam("testTeam", admin, region);
            Long fakeTeamId = 3L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            // mocking
            when(userRepository.findById(fakeAdminId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> teamMemberService.withdraw(fakeAdminId, fakeUserId, fakeTeamId))
                    .hasMessage("해당 사용자가 존재하지 않습니다.")
                    .isInstanceOf(CustomException.class);
        }

        @DisplayName("팀원이 없는 User이면 에러가 발생한다")
        @Test
        public void withdraw3() {

            // given
            Region region = initRegion("당산동");

            User admin = initUser("test1@test.com", "test1", region);
            Long fakeAdminId = 1L;
            ReflectionTestUtils.setField(admin, "id", fakeAdminId);

            User user = initUser("test2@test.com", "test2", region);
            Long fakeUserId = 2L;
            ReflectionTestUtils.setField(user, "id", fakeUserId);

            Team team = initTeam("testTeam", admin, region);
            Long fakeTeamId = 3L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            // mocking
            when(userRepository.findById(fakeAdminId)).thenReturn(Optional.ofNullable(admin));
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> teamMemberService.withdraw(fakeAdminId, fakeUserId, fakeTeamId))
                    .hasMessage("해당 사용자가 존재하지 않습니다.")
                    .isInstanceOf(CustomException.class);
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void withdraw4() {

            // given
            Region region = initRegion("당산동");

            User admin = initUser("test1@test.com", "test1", region);
            Long fakeAdminId = 1L;
            ReflectionTestUtils.setField(admin, "id", fakeAdminId);

            User user = initUser("test2@test.com", "test2", region);
            Long fakeUserId = 2L;
            ReflectionTestUtils.setField(user, "id", fakeUserId);

            Team team = initTeam("testTeam", admin, region);
            Long fakeTeamId = 3L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            // mocking
            when(userRepository.findById(fakeAdminId)).thenReturn(Optional.ofNullable(admin));
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(user));
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> teamMemberService.withdraw(fakeAdminId, fakeUserId, fakeTeamId))
                    .hasMessage("해당 팀이 존재하지 않습니다.")
                    .isInstanceOf(CustomException.class);
        }

        @DisplayName("탈퇴시킬 팀원이 이미 팀원이 아니면 에러가 발생한다")
        @Test
        public void withdraw5() {

            // given
            Region region = initRegion("당산동");

            User admin = initUser("test1@test.com", "test1", region);
            Long fakeAdminId = 1L;
            ReflectionTestUtils.setField(admin, "id", fakeAdminId);

            User user = initUser("test2@test.com", "test2", region);
            Long fakeUserId = 2L;
            ReflectionTestUtils.setField(user, "id", fakeUserId);

            Team team = initTeam("testTeam", admin, region);
            Long fakeTeamId = 3L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);

            TeamMember teamMember = initTeamMember(admin, team, TeamRole.ADMIN);

            // mocking
            when(userRepository.findById(fakeAdminId)).thenReturn(Optional.ofNullable(admin));
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(user));
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.ofNullable(team));
            when(teamMemberRepository.findByUserAndTeam(admin, team)).thenReturn(Optional.ofNullable(teamMember));
            when(teamMemberRepository.findByUserAndTeam(user, team)).thenReturn(Optional.empty());


            // when, then
            assertThatThrownBy(() -> teamMemberService.withdraw(fakeAdminId, fakeUserId, fakeTeamId))
                    .hasMessage("이미 탈퇴한 멤버입니다.")
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    @DisplayName("CheckRoleTest")
    public class CheckRoleTest {

        @DisplayName("팀원의 권한을 확인하고 권한이 ADMIN이 아니면 에러가 발생한다")
        @Test
        public void checkRole() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", user, region);

            TeamMember teamMember = initTeamMember(user, team, TeamRole.MEMBER);

            // mocking
            when(teamMemberRepository.findByUserAndTeam(user, team)).thenReturn(Optional.ofNullable(teamMember));

            // when
            assertThatThrownBy(() -> teamMemberService.checkRole(user, team))
                    .hasMessage("권한이 없습니다.")
                    .isInstanceOf(CustomException.class);
        }

        @DisplayName("존재하지 않는 팀원이면 에러가 발생한다")
        @Test
        public void checkRole2() {

            // given
            Region region = initRegion("당산동");
            User admin = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", admin, region);

            TeamMember teamMember = initTeamMember(admin, team, TeamRole.ADMIN);

            // mocking
            when(teamMemberRepository.findByUserAndTeam(admin, team)).thenReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> teamMemberService.checkRole(admin, team))
                    .hasMessage("해당 소속팀 정보가 존재하지 않습니다.")
                    .isInstanceOf(CustomException.class);
        }
    }

    @Nested
    @DisplayName("CheckAlreadyMemberTest")
    public class CheckAlreadyMemberTest {

        @DisplayName("이미 존재하는 팀원이면 에러가 발생한다")
        @Test
        public void checkAlreadyMember() {

            // given
            Region region = initRegion("당산동");
            User user = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", user, region);

            TeamMember teamMember = initTeamMember(user, team, TeamRole.MEMBER);

            // mocking
            when(teamMemberRepository.findByUserAndTeam(user, team)).thenReturn(Optional.ofNullable(teamMember));

            // when, then
            assertThatThrownBy(() -> teamMemberService.checkAlreadyMember(user, team))
                    .hasMessage("이미 등록된 멤버입니다.")
                    .isInstanceOf(CustomException.class);
        }
    }
}
