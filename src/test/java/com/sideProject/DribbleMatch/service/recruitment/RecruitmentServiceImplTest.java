package com.sideProject.DribbleMatch.service.recruitment;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentSearchParamRequest;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.recruitment.RecruitmentRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.service.team.TeamService;
import com.sideProject.DribbleMatch.service.team.TeamServiceImpl;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Import(QuerydslConfig.class)
class RecruitmentServiceImplTest {
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
    private TeamService teamService;
    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @AfterEach
    void tearDown() {
        recruitmentRepository.deleteAllInBatch();
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

    private Recruitment initRecruitment(String title, Team team) {
        return Recruitment.builder()
                .title(title)
                .content("testRecruitment content")
                .positions(List.of(new Position[]{Position.CENTER}))
                .winning(10)
                .team(team)
                .build();
    }

    private Recruitment initRecruitment(String title, Team team, List<Position> positions) {
        return recruitmentRepository.save(
                Recruitment.builder()
                        .title(title)
                        .content("테스트용 모집글입니다")
                        .winning(0)
                        .positions(positions)
                        .team(team)
                        .build()
        );
    }

    @DisplayName("creat Test(통합)")
    @Nested
    class CreateTest{
        @DisplayName("팀 리더는 모집글을 작성할 수 있다.")
        @Test
        void createRecruitment(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.ADMIN);

            //when
            RecruitmentCreateRequestDto request = new RecruitmentCreateRequestDto(
                    team.getId(),
                    "팀원 모집",
                    "전 포지션 팀원 모집",
                    List.of(Position.CENTER,Position.NO_MATTER),
                    10
            );
            recruitmentService.create(request, leader.getId());

            //then
            List<Recruitment> recruitments = recruitmentRepository.findAll();
            assertThat(recruitments.size()).isEqualTo(1);
            assertThat(recruitments.get(0).getContent()).isEqualTo(request.getContent());
        }

        @DisplayName("팀원이 아니면 없으면 모집글을 쓸 수 없다")
        @Test
        void createRecruitmentNotMemberThrows(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);

            //when
            RecruitmentCreateRequestDto request = new RecruitmentCreateRequestDto(
                    team.getId(),
                    "팀원 모집",
                    "전 포지션 팀원 모집",
                    List.of(Position.CENTER,Position.NO_MATTER),
                    10
            );

            //then
            assertThatThrownBy(() -> recruitmentService.create(request, leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀원이 존재하지 않습니다.");
        }

        @DisplayName("리더가 아니면 없으면 모집글을 쓸 수 없다")
        @Test
        void createRecruitmentNotLeaderThrows(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.MEMBER);

            //when
            RecruitmentCreateRequestDto request = new RecruitmentCreateRequestDto(
                    team.getId(),
                    "팀원 모집",
                    "전 포지션 팀원 모집",
                    List.of(Position.CENTER,Position.NO_MATTER),
                    10
            );

            //then
            assertThatThrownBy(() -> recruitmentService.create(request, leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }
    }

    @DisplayName("update Test(통합)")
    @Nested
    class UpdateTest{
        @DisplayName("팀 리더는 모집글을 수정 할 수 있다.")
        @Test
        void updateRecruitment(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.ADMIN);
            Recruitment recruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            //when
            RecruitmentUpdateRequestDto request = new RecruitmentUpdateRequestDto(
                    "팀원 모집",
                    "전 포지션 팀원 모집",
                    List.of(Position.CENTER,Position.NO_MATTER),
                    10
            );
            recruitmentService.update(request, recruitment.getId(),leader.getId());

            //then
            List<Recruitment> recruitments = recruitmentRepository.findAll();
            assertThat(recruitments.size()).isEqualTo(1);
            assertThat(recruitments.get(0).getContent()).isEqualTo(request.getContent());
        }

        @DisplayName("팀원이 아니면 없으면 모집글을 수정할 수 없다")
        @Test
        void updateRecruitmentNotMemberThrows(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            Recruitment recruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            //when
            RecruitmentUpdateRequestDto request = new RecruitmentUpdateRequestDto(
                    "팀원 모집",
                    "전 포지션 팀원 모집",
                    List.of(Position.CENTER,Position.NO_MATTER),
                    10
            );

            //then
            assertThatThrownBy(() -> recruitmentService.update(request, recruitment.getId(),leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀원이 존재하지 않습니다.");
        }

        @DisplayName("리더가 아니면 없으면 모집글을 쓸 수 없다")
        @Test
        void createRecruitmentNotLeaderThrows(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.MEMBER);
            Recruitment recruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            //when
            RecruitmentUpdateRequestDto request = new RecruitmentUpdateRequestDto(
                    "팀원 모집",
                    "전 포지션 팀원 모집",
                    List.of(Position.CENTER,Position.NO_MATTER),
                    10
            );

            //then
            assertThatThrownBy(() -> recruitmentService.update(request, recruitment.getId(),leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }
    }

    @DisplayName("delete Test(통합)")
    @Nested
    class DeleteTest{
        @DisplayName("팀 리더는 모집글을 삭제 할 수 있다.")
        @Test
        void updateRecruitment(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.ADMIN);
            Recruitment recruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            //when
            recruitmentService.delete(recruitment.getId(),leader.getId());

            //then
            List<Recruitment> recruitments = recruitmentRepository.findAll();
            assertThat(recruitments.size()).isEqualTo(0);
        }

        @DisplayName("팀원이 아니면 없으면 모집글을 수정할 수 없다")
        @Test
        void deleteeRecruitmentNotMemberThrows(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            Recruitment recruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            //when
            //then
            assertThatThrownBy(() -> recruitmentService.delete(recruitment.getId(),leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀원이 존재하지 않습니다.");
        }

        @DisplayName("리더가 아니면 없으면 모집글을 쓸 수 없다")
        @Test
        void deleteRecruitmentNotLeaderThrows(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.MEMBER);
            Recruitment recruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            //when
            //then
            assertThatThrownBy(() -> recruitmentService.delete(recruitment.getId(),leader.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다");
        }
    }

    @DisplayName("findById Test(통합)")
    @Nested
    class findByIdTest{
        @DisplayName("아이디로 모집글을 검색할 수 있다.")
        @Test
        void findById(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.ADMIN);
            Recruitment recruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            //when
            RecruitmentResponseDto dto = recruitmentService.findById(recruitment.getId());

            //then
            assertThat(dto.getTitle()).isEqualTo(recruitment.getTitle());
            assertThat(dto.getContent()).isEqualTo(recruitment.getContent());
            assertThat(dto.getTeam().getLeaderNickName()).isEqualTo(recruitment.getTeam().getLeader().getNickName());

        }

        @DisplayName("없는 아이디로 조회시 예외가 발생한다.")
        @Test
        void findByIdUnknownId(){
            //given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com","test", region);
            Team team = initTeam("testTeam", leader, region);
            initTeamMember(leader,team,TeamRole.ADMIN);

            //when
            //then
            assertThatThrownBy(() -> recruitmentService.findById(1L))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 모집글이 존재하지 않습니다.");

        }

    }

    @Nested
    @DisplayName("RecruitmentFindTest(통합)")
    class Find{

        @DisplayName("모집글을 조회할 수 있다.")
        @Test
        void recruitmentFindTest(){
            //given
            Region region = initRegion("서울특별시","당산동");
            User leader = initUser("tes1t@test.com","test1", region);
            Team team = initTeam("testTeam", leader, region);
            Recruitment recruitment = initRecruitment("테스트 모집글", team, new ArrayList<>());

            //when
            Pageable pageable = PageRequest.of(0, 2);
            RecruitmentSearchParamRequest param = new RecruitmentSearchParamRequest(
                    null,
                    null,
                    List.of()
            );

            Page<RecruitmentResponseDto> result = recruitmentService.find(pageable,param);

            //then
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.getContent().get(0).getContent()).isEqualTo(recruitment.getContent());
            assertThat(result.getContent().get(0).getTitle()).isEqualTo(recruitment.getTitle());
        }

        @DisplayName("모집글을 지역명으로 조회할 수 있다.")
        @Test
        void recruitmentFindTestByRegion(){
            //given
            Region region1 = initRegion("서울특별시","당산동");
            User leader1 = initUser("test12@test.com","test", region1);
            Team team1 = initTeam("testTeam", leader1, region1);

            Region region2 = initRegion("울산광역시","당산동");
            User leader2 = initUser("test2@test.com","test2", region2);
            Team team2 = initTeam("testTeam2", leader2, region2);

            Recruitment recruitment1 = initRecruitment("테스트 모집글", team1,new ArrayList<>());
            Recruitment recruitment2 = initRecruitment("테스트 모집글", team2,new ArrayList<>());

            //when
            Pageable pageable = PageRequest.of(0, 2);
            RecruitmentSearchParamRequest param = new RecruitmentSearchParamRequest(
                    "서울특별시",
                    null,
                    List.of()
            );

            Page<RecruitmentResponseDto> result = recruitmentService.find(pageable,param);

            //then
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.getContent().get(0).getContent()).isEqualTo(recruitment1.getContent());
            assertThat(result.getContent().get(0).getTitle()).isEqualTo(recruitment1.getTitle());
        }

        @DisplayName("모집글을 포지션으로 조회할 수 있다.")
        @Test
        void recruitmentFindTestByPosition() {
            //given
            Region region1 = initRegion("서울특별시","당산동");
            User leader1 = initUser("test12@test.com","test", region1);
            Team team1 = initTeam("testTeam", leader1, region1);

            Region region2 = initRegion("울산광역시","당산동");
            User leader2 = initUser("test2@test.com","test2", region2);
            Team team2 = initTeam("testTeam2", leader2, region2);

            Recruitment recruitment1 = initRecruitment(
                    "테스트 모집글1",
                    team1,
                    List.of(new Position[]{Position.CENTER, Position.POINT_GUARD, Position.POWER_FORWARD}));
            Recruitment recruitment2 = initRecruitment(
                    "테스트 모집글2",
                    team2,
                    List.of(new Position[]{Position.NO_MATTER}));

            //when
            Pageable pageable = PageRequest.of(0, 2);
            RecruitmentSearchParamRequest param = new RecruitmentSearchParamRequest(
                    null,
                    null,
                    Arrays.asList(Position.CENTER)
            );

            Page<RecruitmentResponseDto> result = recruitmentService.find(pageable,param);

            //then
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.getContent().get(0).getContent()).isEqualTo(recruitment1.getContent());
            assertThat(result.getContent().get(0).getTitle()).isEqualTo(recruitment1.getTitle());
        }
    }

}