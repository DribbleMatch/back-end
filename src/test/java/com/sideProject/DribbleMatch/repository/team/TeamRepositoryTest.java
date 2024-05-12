package com.sideProject.DribbleMatch.repository.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class TeamRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

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
        return Team.builder()
                .name(name)
                .winning(10)
                .leader(leader)
                .region(region)
                .info("test")
                .maxNumber(10)
                .build();
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

            // when
            Team savedTeam = teamRepository.save(team);

            // then
            assertThat(savedTeam).isNotNull();
            assertThat(savedTeam).isEqualTo(team);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createTeam2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = Team.builder()
//                    .name(name)
                    .winning(10)
                    .leader(leader)
                    .build();

            // when, then
            assertThatThrownBy(() -> teamRepository.save(team))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @DisplayName("name이 중복이면 에러가 발생한다")
        @Test
        public void createTeam3() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team1 = initTeam("testTeam", leader, region);
            Team team2 = initTeam("testTeam", leader, region);

            // when
            teamRepository.save(team1);

            // then
            assertThatThrownBy(() -> teamRepository.save(team2))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectTeamTest")
    public class SelectTeamTest {

        @DisplayName("Team을 조회한다")
        @Test
        public void selectTeam() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team savedTeam = teamRepository.save(initTeam("testTeam", leader, region));

            // when
            Team selectedTeam = teamRepository.findById(savedTeam.getId()).get();

            // then
            assertThat(selectedTeam).isNotNull();
            assertThat(selectedTeam).isEqualTo(savedTeam);
        }

        @DisplayName("모든 Team을 조회한다 (Page)")
        @Test
        public void selectTeam2() {

            // given
            Region region1 = initRegion("당산동1");
            Region region2 = initRegion("당산동2");
            Region region3 = initRegion("당산동3");
            Region region4 = initRegion("당산동4");
            User leader1 = initUser("test1@test.com", "test1", region1);
            User leader2= initUser("test2@test.com", "test2", region2);
            User leader3 = initUser("test3@test.com", "test3", region3);
            User leader4 = initUser("test4@test.com", "test4", region4);

            Team savedTeam1 = teamRepository.save(initTeam("testTeam1", leader1, region1));
            Team savedTeam2 = teamRepository.save(initTeam("testTeam2", leader2, region2));
            Team savedTeam3 = teamRepository.save(initTeam("testTeam3", leader3, region3));
            Team savedTeam4 = teamRepository.save(initTeam("testTeam4", leader4, region4));

            Pageable pageable = PageRequest.of(1, 2);

            // when
            Page<Team> teams = teamRepository.findAll(pageable);

            // then
            assertThat(teams.getTotalPages()).isEqualTo(2);
            assertThat(teams.getTotalElements()).isEqualTo(4);
            assertThat(teams.getContent().size()).isEqualTo(2);

            assertThat(teams.get().toList().get(0)).isEqualTo(savedTeam3);
        }

        @DisplayName("Name으로 Team을 조회한다")
        @Test
        public void selectTeam3() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team savedTeam = teamRepository.save(initTeam("testTeam", leader, region));

            // when
            Team selectedTeam = teamRepository.findByName(savedTeam.getName()).get();

            // then
            assertThat(selectedTeam).isNotNull();
            assertThat(selectedTeam).isEqualTo(savedTeam);
        }

        @DisplayName("없는 Team이면 에러가 발생한다")
        @Test
        public void selectTeam4() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team savedTeam = teamRepository.save(initTeam("testTeam", leader, region));

            // when, then
            assertThatThrownBy(() -> teamRepository.findById(savedTeam.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 팀이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("UpdateTeamTest")
    public class UpdateTeamTest {

        @DisplayName("팀 정보를 수정한다")
        @Test
        public void updateTeam() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team savedTeam = teamRepository.save(initTeam("testTeam1", leader, region));

            User newLeader = initUser("test2@test.com", "test2", region);
            Region newRegion = initRegion("문래동");

            // when
            ReflectionTestUtils.setField(savedTeam, "name", "testTeam2");
            ReflectionTestUtils.setField(savedTeam, "winning", 11);
            ReflectionTestUtils.setField(savedTeam, "leader", newLeader);
            ReflectionTestUtils.setField(savedTeam, "region", newRegion);


            Team updatedTeam = teamRepository.findById(savedTeam.getId()).get();

            // then
            assertThat(updatedTeam.getName()).isEqualTo("testTeam2");
            assertThat(updatedTeam.getRegion()).isEqualTo(newRegion);
            assertThat(updatedTeam.getWinning()).isEqualTo(11);
            assertThat(updatedTeam.getLeader()).isEqualTo(newLeader);
        }
    }

    @Nested
    @DisplayName("DeleteTeamTest")
    public class DeleteTeamTest {

        @DisplayName("팀을 삭제한다")
        @Test
        public void deleteTeam() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team savedTeam = teamRepository.save(initTeam("testTeam", leader, region));

            // when
            teamRepository.deleteById(savedTeam.getId());

            // then
            assertThat(teamRepository.findById(savedTeam.getId()).isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("FindByRegionIds")
    public class FindByRegionIds {

        @DisplayName("Region Id의 배열에 포함되는 Region을 가진 Team을 조회한다")
        @Test
        public void findByRegionIds() {

            // given
            Region region1 = initRegion("당산동");
            Region region2 = initRegion("문래동");

            User leader1 = initUser("test1@test.com", "test1", region1);
            User leader2 = initUser("test2@test.com", "test2", region1);
            User leader3 = initUser("test3@test.com", "test3", region1);
            User leader4 = initUser("test4@test.com", "test4", region1);

            Team team1 = teamRepository.save(initTeam("testTeam1", leader1, region1));
            Team team2 = teamRepository.save(initTeam("testTeam2", leader2, region1));
            Team team3 = teamRepository.save(initTeam("testTeam3", leader3, region2));
            Team team4 = teamRepository.save(initTeam("testTeam4", leader4, region2));

            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<Team> teams1 = teamRepository.findByRegionIds(pageable, List.of(region1.getId()));
            Page<Team> teams2 = teamRepository.findByRegionIds(pageable, List.of(region2.getId()));
            Page<Team> teams3 = teamRepository.findByRegionIds(pageable, List.of(region1.getId(), region2.getId()));

            // then
            assertThat(teams1.getContent().size()).isEqualTo(2);
            assertThat(teams1.getTotalPages()).isEqualTo(1);
            assertThat(teams1.getTotalElements()).isEqualTo(2);
            assertThat(teams1.get().toList().get(0)).isEqualTo(team1);
            assertThat(teams1.get().toList().get(1)).isEqualTo(team2);

            assertThat(teams2.getContent().size()).isEqualTo(2);
            assertThat(teams2.getTotalPages()).isEqualTo(1);
            assertThat(teams2.getTotalElements()).isEqualTo(2);
            assertThat(teams2.get().toList().get(0)).isEqualTo(team3);
            assertThat(teams2.get().toList().get(1)).isEqualTo(team4);

            assertThat(teams3.getContent().size()).isEqualTo(2);
            assertThat(teams3.getTotalPages()).isEqualTo(2);
            assertThat(teams3.getTotalElements()).isEqualTo(4);
            assertThat(teams3.get().toList().get(0)).isEqualTo(team1);
            assertThat(teams3.get().toList().get(1)).isEqualTo(team2);
        }
    }
}
