package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
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

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;
    @Mock
    private TeamApplicationRepository teamApplicationRepository;


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

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
                .leader(leader)
                .region(region)
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
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            TeamCreateRequestDto request = TeamCreateRequestDto.builder()
                    .name("testTeam")
                    .regionString("서울시 영등포구 당산동")
                    .build();

            // mocking
            when(regionRepository.findByRegionString("서울시 영등포구 당산동")).thenReturn(Optional.ofNullable(region));

            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.save(any(Team.class))).thenReturn(team);

            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(leader));

            // when
            Long teamId =  teamService.createTeam(leader.getId(), request);

            // then
            assertThat(teamId).isEqualTo(fakeTeamId);
        }

        @DisplayName("name이 중복이면 에러가 발생한다")
        @Test
        public void createTeam2() {
            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            TeamCreateRequestDto request = TeamCreateRequestDto.builder()
                    .name("testTeam")
                    .regionString("서울시 영등포구 당산동")
                    .build();

            // mocking
            when(teamRepository.findByName("testTeam")).thenReturn(Optional.ofNullable(team));

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
            Region region = initRegion("당산동");
            User leader = initUser("test1@test.com", "test1", region);
            Team team = initTeam("testTeam", leader, region);

            Region newRegion = initRegion("문래동");

            User newLeader = initUser("test2@test.com", "test2", region);
            Long fakeNewLeaderId = 1L;
            ReflectionTestUtils.setField(newLeader, "id", fakeNewLeaderId);

            TeamUpdateRequestDto request = TeamUpdateRequestDto.builder()
                    .name("newTestTeam")
                    .regionString("서울시 영등포구 문래동")
                    .leaderId(newLeader.getId())
                    .build();


            // mocking
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.ofNullable(team));

            when(userRepository.findById(fakeNewLeaderId)).thenReturn(Optional.ofNullable(newLeader));
            when(regionRepository.findByRegionString("서울시 영등포구 문래동")).thenReturn(Optional.ofNullable(newRegion));

            // when
            Long teamId =  teamService.updateTeam(fakeTeamId, request);

            // then
            assertThat(teamId).isEqualTo(fakeTeamId);
        }

        @DisplayName("name이 중복이면 에러가 발생한다")
        @Test
        public void updateTeam2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);

            TeamUpdateRequestDto request = TeamUpdateRequestDto.builder()
                    .name("testTeam")
                    .regionString("서울시 영등포구 당산동")
                    .leaderId(leader.getId())
                    .build();

            Team team = initTeam("testTeam", leader, region);

            // mocking
            when(teamRepository.findByName("testTeam")).thenReturn(Optional.ofNullable(team));

            // when, then
            assertThatThrownBy(() -> teamService.updateTeam(team.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("팀 이름이 이미 존재합니다.");
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void updateTeam3() {

            // given
            Region region = initRegion("당산동");

            User leader = initUser("test@test.com", "test", region);
            Long fakeUserId = 1L;
            ReflectionTestUtils.setField(leader, "id", fakeUserId);

            Team team = initTeam("testTeam", leader, region);

            TeamUpdateRequestDto request = TeamUpdateRequestDto.builder()
                    .name("testTeam")
                    .regionString("서울시 영등포구 당산동")
                    .leaderId(leader.getId())
                    .build();

            // mocking
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.findByName("testTeam")).thenReturn(Optional.empty());
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.empty());

            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(leader));

            when(regionRepository.findByRegionString("서울시 영등포구 당산동")).thenReturn(Optional.ofNullable(region));

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
            Region region1 = initRegion("당산동");
            Region region2 = initRegion("문래동");
            ReflectionTestUtils.setField(region1, "id", 1L);
            ReflectionTestUtils.setField(region2, "id", 2L);

            User leader1 = initUser("test1@test.com", "test1", region1);
            User leader2 = initUser("test2@test.com", "test2", region2);

            Team team1 = initTeam("testTeam1", leader1, region1);
            Team team2 = initTeam("testTeam2", leader1, region2);
            Team team3 = initTeam("testTeam3", leader2, region1);
            Team team4 = initTeam("testTeam4", leader2, region2);

            List<Team> teams = List.of(team1, team2, team3, team4);
            Pageable pageable = PageRequest.of(1, 2);
            Page<Team> teamsPage = convertToPage(teams, pageable);

            // mocking
            when(regionRepository.findRegionStringById(1L)).thenReturn(Optional.ofNullable("서울특별시 영등포구 당산동"));
            when(regionRepository.findRegionStringById(2L)).thenReturn(Optional.ofNullable("서울특별시 영등포구 문래동"));

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

        @DisplayName("전체 Team을 지역별로 조회한다")
        @Test
        public void findAllTeams2() {

            // given
            String requestRegionString1 = "서울특별시 영등포구";
            String requestRegionString2 = "서울특별시 영등포구 당산동";
            String requestRegionString3 = "서울특별시 영등포구 문래동";
            String requestRegionString4 = "서울특별시 마포구 합정동";

            Region region1 = initRegion(null);
            Region region2 = initRegion("당산동");
            Region region3 = initRegion("문래동");
            Region region4 = initRegion("합정동");
            ReflectionTestUtils.setField(region1, "id", 1L);
            ReflectionTestUtils.setField(region2, "id", 2L);
            ReflectionTestUtils.setField(region3, "id", 3L);
            ReflectionTestUtils.setField(region4, "id", 4L);

            User leader = initUser("test@test.com", "test", region1);

            Team team1 = initTeam("testTeam1", leader, region1);
            Team team2 = initTeam("testTeam2", leader, region2);
            Team team3 = initTeam("testTeam3", leader, region3);
            Team team4 = initTeam("testTeam4", leader, region4);

            Pageable pageable = PageRequest.of(0, 2);

            // mocking
            when(regionRepository.findIdsByRegionString("서울특별시 영등포구")).thenReturn(List.of(1L, 2L, 3L));
            when(regionRepository.findIdsByRegionString("서울특별시 영등포구 당산동")).thenReturn(List.of(2L));
            when(regionRepository.findIdsByRegionString("서울특별시 마포구 합정동")).thenReturn(List.of(4L));
            when(regionRepository.findRegionStringById(1L)).thenReturn(Optional.ofNullable(requestRegionString1));
            when(regionRepository.findRegionStringById(2L)).thenReturn(Optional.ofNullable(requestRegionString2));
            when(regionRepository.findRegionStringById(4L)).thenReturn(Optional.ofNullable(requestRegionString4));

            Page<Team> teamsPage1 = convertToPage(List.of(team1, team2, team3), pageable);
            Page<Team> teamsPage2 = convertToPage(List.of(team2), pageable);
            Page<Team> teamsPage3 = convertToPage(List.of(team4), pageable);

            when(teamRepository.findByRegionIds(pageable, List.of(1L, 2L, 3L))).thenReturn(teamsPage1);
            when(teamRepository.findByRegionIds(pageable, List.of(2L))).thenReturn(teamsPage2);
            when(teamRepository.findByRegionIds(pageable, List.of(4L))).thenReturn(teamsPage3);

            // when
            Page<TeamResponseDto> result1 = teamService.findAllTeams(pageable, requestRegionString1);
            Page<TeamResponseDto> result2 = teamService.findAllTeams(pageable, requestRegionString2);
            Page<TeamResponseDto> result3 = teamService.findAllTeams(pageable, requestRegionString4);

            // then
            assertThat(result1.getTotalPages()).isEqualTo(2);
            assertThat(result1.getTotalElements()).isEqualTo(3);
            assertThat(result1.getContent().size()).isEqualTo(2);
            assertThat(result1.get().toList().get(0).getName()).isEqualTo("testTeam1");
            assertThat(result1.get().toList().get(1).getName()).isEqualTo("testTeam2");

            assertThat(result2.getTotalPages()).isEqualTo(1);
            assertThat(result2.getTotalElements()).isEqualTo(1);
            assertThat(result2.getContent().size()).isEqualTo(1);
            assertThat(result2.get().toList().get(0).getName()).isEqualTo("testTeam2");

            assertThat(result3.getTotalPages()).isEqualTo(1);
            assertThat(result3.getTotalElements()).isEqualTo(1);
            assertThat(result3.getContent().size()).isEqualTo(1);
            assertThat(result3.get().toList().get(0).getName()).isEqualTo("testTeam4");
        }
    }

    @Nested
    @DisplayName("FindTeamTest")
    public class FindTeamTest {

        @DisplayName("Team을 조회한다")
        @Test
        public void findTeam() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            // mocking
            Long fakeTeamId = 1L;
            ReflectionTestUtils.setField(team, "id", fakeTeamId);
            when(teamRepository.findById(fakeTeamId)).thenReturn(Optional.ofNullable(team));

            Long fakeRegionId = 1L;
            ReflectionTestUtils.setField(region, "id", fakeRegionId);
            when(regionRepository.findRegionStringById(fakeRegionId)).thenReturn(Optional.ofNullable("서울특별시 영등포구 당산동"));

            // when
            TeamResponseDto foundTeam = teamService.findTeam(fakeTeamId);

            // then
            assertThat(foundTeam.getName()).isEqualTo(team.getName());
            assertThat(foundTeam.getRegionString()).isEqualTo("서울특별시 영등포구 당산동");
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
