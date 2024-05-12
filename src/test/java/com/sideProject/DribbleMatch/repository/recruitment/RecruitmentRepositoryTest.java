package com.sideProject.DribbleMatch.repository.recruitment;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.repository.recruitment.RecruitmentRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class RecruitmentRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

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

    private Recruitment initRecruitment(String title, Team team) {
        return Recruitment.builder()
                .title(title)
                .content("testRecruitment content")
                .positions(List.of(new Position[]{Position.CENTER}))
                .winning(10)
                .team(team)
                .build();
    }

    @Nested
    @DisplayName("CreateRecruitmentTest")
    public class CreateRecruitmentTest {

        @DisplayName("Recruitment를 생성한다")
        @Test
        public void createRecruitment() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            Recruitment recruitment = initRecruitment("testRecruitment", team);

            // when
            Recruitment savedrecruitment = recruitmentRepository.save(recruitment);

            // then
            assertThat(savedrecruitment).isNotNull();
            assertThat(savedrecruitment).isEqualTo(recruitment);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createRecruitment2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            Recruitment recruitment = initRecruitment(null, team);

            // when, then
            assertThatThrownBy(() -> recruitmentRepository.save(recruitment))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectRecruitmentTest")
    public class SelectRecruitmentTest {

        @DisplayName("Recruitment를 조회한다")
        @Test
        public void selectRecruitment() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            Recruitment savedRecruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            // when
            Recruitment selectedRecruitment = recruitmentRepository.findById(savedRecruitment.getId()).get();

            // then
            assertThat(selectedRecruitment).isNotNull();
            assertThat(selectedRecruitment).isEqualTo(savedRecruitment);
        }

        @DisplayName("모든 Recruitment를 조회한다")
        @Test
        public void selectRecruitment2() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            Recruitment savedRecruitment1 = recruitmentRepository.save(initRecruitment("testRecruitment", team));
            Recruitment savedRecruitment2 = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            // when
            List<Recruitment> recruitments = recruitmentRepository.findAll();

            // then
            assertThat(recruitments.size()).isEqualTo(2);
            assertThat(recruitments.contains(savedRecruitment1)).isTrue();
            assertThat(recruitments.contains(savedRecruitment2)).isTrue();
        }

        @DisplayName("없는 Recruitment면 에러가 발생한다")
        @Test
        public void selectRecruitment3() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            Recruitment savedRecruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            // when, then
            assertThatThrownBy(() -> recruitmentRepository.findById(savedRecruitment.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_RECRUITMENT_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 모집글이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteRecruitmentTest")
    public class DeleteRecruitmentTest {

        @DisplayName("Recruitment을 삭제한다")
        @Test
        public void deleteRecruitment() {

            // given
            Region region = initRegion("당산동");
            User leader = initUser("test@test.com", "test", region);
            Team team = initTeam("testTeam", leader, region);

            Recruitment savedRecruitment = recruitmentRepository.save(initRecruitment("testRecruitment", team));

            // when
            recruitmentRepository.deleteById(savedRecruitment.getId());

            // then
            assertThat(recruitmentRepository.findById(savedRecruitment.getId()).isPresent()).isFalse();
        }
    }
}
