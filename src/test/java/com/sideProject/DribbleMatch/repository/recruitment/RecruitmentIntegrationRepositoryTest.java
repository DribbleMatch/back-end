package com.sideProject.DribbleMatch.repository.recruitment;

import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentSearchParamRequest;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
//@Import(QuerydslConfig.class)
public class RecruitmentIntegrationRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    RecruitmentRepository recruitmentRepository;
    @Autowired
    RegionRepository regionRepository;

    @AfterEach
    void tearDown() {
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

            Page<Recruitment> result = recruitmentRepository.find(pageable,param);

            //then
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.getContent().get(0).getContent()).isEqualTo(recruitment.getContent());
            assertThat(result.getContent().get(0).getTitle()).isEqualTo(recruitment.getTitle());
            assertThat(result.getContent().get(0).getTeam().getId()).isEqualTo(recruitment.getTeam().getId());
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

            Page<Recruitment> result = recruitmentRepository.find(pageable,param);

            //then
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.getContent().get(0).getContent()).isEqualTo(recruitment1.getContent());
            assertThat(result.getContent().get(0).getTitle()).isEqualTo(recruitment1.getTitle());
            assertThat(result.getContent().get(0).getTeam().getId()).isEqualTo(recruitment1.getTeam().getId());
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

            Page<Recruitment> result = recruitmentRepository.find(pageable,param);

            //then
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.getContent().get(0).getContent()).isEqualTo(recruitment1.getContent());
            assertThat(result.getContent().get(0).getTitle()).isEqualTo(recruitment1.getTitle());
            assertThat(result.getContent().get(0).getTeam().getId()).isEqualTo(recruitment1.getTeam().getId());
        }
    }
}
