package com.sideProject.DribbleMatch.domain.team.service;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.domain.team.dto.TeamResponseDto;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.team.repository.TeamRepository;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import com.sideProject.DribbleMatch.domain.user.repository.UserRepository;
import com.sideProject.DribbleMatch.domain.userTeam.entity.UserTeam;
import com.sideProject.DribbleMatch.domain.userTeam.repository.UserTeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    private TeamServiceImpl teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserRepository userRepository;

    private User initUser(String email, String nickName) {
        return User.builder()
                .email(email)
                .password("test1234")
                .nickName(nickName)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .build();
    }

    private Team initTeam(String name, User leader) {
        return Team.builder()
                .name(name)
                .region("서울시 영등포구")
                .winning(10)
                .leader(leader)
                .build();
    }

    private Page<Team> convertToPage(List<Team> teams, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), teams.size());
        return new PageImpl<>(teams.subList(start, end), pageable, teams.size());
    }

    @Nested
    @DisplayName("CreateTeamTest")
    public class CreateTeamTest {

        @DisplayName("Team을 생성한다")
        @Test
        public void createTeam() {

            // given
            User leader = initUser("test@test.com", "test");
            Team team = initTeam("testTeam", leader);

            TeamCreateRequestDto request = TeamCreateRequestDto.builder()
                    .name("testTeam")
                    .region("서울시 영등포구")
                    .build();

            // mocking
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.save(any(Team.class))).thenReturn(team);

            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.of(leader));

            // when
            Long teamId =  teamService.createTeam(leader.getId(), request);

            // then
            assertThat(teamId).isEqualTo(fakeTeamId);
        }

        @DisplayName("name이 중복이면 에러가 발생한다")
        @Test
        public void createTeam2() {
            // given
            User leader = initUser("test@test.com", "test");
            Team team = initTeam("testTeam", leader);

            TeamCreateRequestDto request = TeamCreateRequestDto.builder()
                    .name("testTeam")
                    .region("서울시 영등포구")
                    .build();

            // mocking
            when(teamRepository.findByName("testTeam")).thenReturn(Optional.of(team));

            // when
            assertThatThrownBy(() -> teamService.createTeam(leader.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("팀 이름이 이미 존재합니다.");

        }
    }

    @Nested
    @DisplayName("UpdateTeamTest")
    public class UpdateTeamTest {
        @DisplayName("Team 정보를 수정한다")
        @Test
        public void updateTeam() {
            // given
            User leader = initUser("test@test.com", "test");
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader);

            TeamUpdateRequestDto request = TeamUpdateRequestDto.builder()
                    .name("testTeam")
                    .region("서울시 영등포구")
                    .leaderId(leader.getId())
                    .build();

            // mocking
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.of(team));

            when(userRepository.findById(fakeUserId)).thenReturn(Optional.of(leader));

            // when
            Long teamId =  teamService.updateTeam(fakeTeamId, request);

            // then
            assertThat(teamId).isEqualTo(fakeTeamId);
        }

        @DisplayName("name이 중복이면 에러가 발생한다")
        @Test
        public void updateTeam2() {

            // given
            User leader = initUser("test@test.com", "test");
            Team team = initTeam("testTeam", leader);

            TeamUpdateRequestDto request = TeamUpdateRequestDto.builder()
                    .name("testTeam")
                    .region("서울시 영등포구")
                    .leaderId(leader.getId())
                    .build();

            // mocking
            when(teamRepository.findByName("testTeam")).thenReturn(Optional.of(team));

            // when, then
            assertThatThrownBy(() -> teamService.updateTeam(team.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("팀 이름이 이미 존재합니다.");
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void updateTeam3() {

            // given
            User leader = initUser("test@test.com", "test");
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader);

            TeamUpdateRequestDto request = TeamUpdateRequestDto.builder()
                    .name("testTeam")
                    .region("서울시 영등포구")
                    .leaderId(leader.getId())
                    .build();

            // mocking
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.findByName("testTeam")).thenReturn(Optional.empty());

            when(userRepository.findById(fakeUserId)).thenReturn(Optional.of(leader));

            // when, then
            assertThatThrownBy(() -> teamService.updateTeam(fakeTeamId, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteTeamTest")
    public class DeleteTeamTest {

        @DisplayName("Team을 삭제한다")
        @Test
        public void deleteTeam() {

            // given
            Long teamId = 1L;

            // mocking
            doNothing().when(teamRepository).deleteById(teamId);

            // when
            String result = teamService.deleteTeam(teamId);

            // then
            assertThat(result).isEqualTo("팀이 삭제되었습니다.");
        }
    }

    @Nested
    @DisplayName("FindAllTeamsTest")
    public class FindAllTeamsTest {

        @DisplayName("전체 Team을 조회한다")
        @Test
        public void findAllTeams() {

            // given
            User leader = initUser("test@test.com", "test");
            Team team1 = initTeam("testTeam1", leader);
            Team team2 = initTeam("testTeam2", leader);
            Team team3 = initTeam("testTeam3", leader);
            Team team4 = initTeam("testTeam4", leader);

            List<Team> teams = List.of(team1, team2, team3, team4);
            Pageable pageable = PageRequest.of(1, 2);
            Page<Team> teamsPage = convertToPage(teams, pageable);

            // mocking
            when(teamRepository.findAll(pageable)).thenReturn(teamsPage);

            // when
            Page<TeamResponseDto> result = teamService.findAllTeams(pageable, "ALL");

            // then
            assertThat(result.getTotalElements()).isEqualTo(4);
            assertThat(result.getContent().size()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(2);

            assertThat(result.get().toList().get(0).getName()).isEqualTo("testTeam3");
            assertThat(result.get().toList().get(1).getName()).isEqualTo("testTeam4");
        }

        @DisplayName("전체 Team을 조건별로 조회한다")
        @Test
        public void findAllTeams2() {
            //todo: Team.region 세부화 후 다시 구현
        }
    }

    @Nested
    @DisplayName("FindTeamTest")
    public class FindTeamTest {

        @DisplayName("Team을 조회한다")
        @Test
        public void findTeam() {

            // given
            User leader = initUser("test@test.com","test");
            Team team = initTeam("testTeam", leader);

            // mocking
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.of(team));

            // when
            TeamResponseDto foundTeam = teamService.findTeam(fakeTeamId);

            // then
            assertThat(foundTeam.getName()).isEqualTo(team.getName());
            assertThat(foundTeam.getRegion()).isEqualTo(team.getRegion());
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void  findTeam2() {

            // mocking
            Long fakeTeamId = 1L;
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> teamService.findTeam(fakeTeamId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }
    }
}
