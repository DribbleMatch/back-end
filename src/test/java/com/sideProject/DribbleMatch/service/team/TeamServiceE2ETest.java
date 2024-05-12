package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.joinTeam.TeamJoin;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamJoin.TeamJoinRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Profile("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class TeamServiceE2ETest {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private TeamJoinRepository teamJoinRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamServiceImpl teamService;

    @AfterEach
    void tearDown() {
        teamJoinRepository.deleteAllInBatch();
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
                    .teamId(team.getId())
                    .introduce("가난한 대학생")
                    .build();

            // when
            teamService.join(request, member.getId());

            // then
            List<TeamJoin> teamJoins = teamJoinRepository.findAll();
            assertThat(teamJoins.size()).isEqualTo(1);
            assertThat(teamJoins.get(0).getTeam().getId()).isEqualTo(team.getId());
            assertThat(teamJoins.get(0).getUser().getId()).isEqualTo(member.getId());
        }

        @DisplayName("이미 가입된 팀원이 신청하면 예외가 발생한다")
        @Test
        public void joinTeamAlreayUser() {

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
                    .teamId(team.getId())
                    .introduce("가난한 대학생")
                    .build();

            // when //then
            assertThatThrownBy(() -> teamService.join(request, member.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 등록된 멤버입니다");
        }

    }
}
