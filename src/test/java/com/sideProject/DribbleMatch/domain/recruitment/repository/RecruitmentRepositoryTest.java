package com.sideProject.DribbleMatch.domain.recruitment.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.recruitment.entity.Recruitment;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.team.repository.TeamRepository;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RecruitmentRepositoryTest {

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    TeamRepository teamRepository;

    private Team initTeam(String name) {
        return teamRepository.save(Team.builder()
                .name(name)
                .region("서울")
                .winning(10)
                .build());
    }

    private Recruitment initRecruitment(Team team) {
        return Recruitment.builder()
                .title("testRecruitment")
                .content("testRecruitment content")
                .position(Position.CENTER)
                .winning(10)
                .team(team)
                .build();
    }

    @Nested
    @DisplayName("createRecruitmentTest")
    public class createRecruitmentTest {

        @DisplayName("Recruitment를 생성한다")
        @Test
        public void createRecruitment() {

            // given
            Team team = initTeam("testTeam");
            Recruitment recruitment = initRecruitment(team);

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
            Team team = initTeam("testTeam");
            Recruitment recruitment = initRecruitment(null);

            // when, then
            assertThatThrownBy(() -> recruitmentRepository.save(recruitment))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("팀이 입력되지 않았습니다.");
        }
    }

    @Nested
    @DisplayName("selectRecruitmentTest")
    public class selectRecruitmentTest {

        @DisplayName("Recruitment를 조회한다")
        @Test
        public void selectRecruitment() {

            // given
            Recruitment savedRecruitment = recruitmentRepository.save(initRecruitment(initTeam("testTeam")));

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
            Recruitment savedRecruitment1 = recruitmentRepository.save(initRecruitment(initTeam("testTeam1")));
            Recruitment savedRecruitment2 = recruitmentRepository.save(initRecruitment(initTeam("testTeam2")));

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
            Recruitment savedRecruitment = recruitmentRepository.save(initRecruitment(initTeam("testTeam")));

            // when, then
            assertThatThrownBy(() -> recruitmentRepository.findById(savedRecruitment.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.INVALID_RECRUITMENT_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 모집글이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteRecruitmentTest")
    public class deleteRecruitmentTest {

        @DisplayName("Recruitment을 생성한다")
        @Test
        public void deleteRecruitment() {

            // given
            Recruitment savedRecruitment = recruitmentRepository.save(initRecruitment(initTeam("testTeam")));

            // when
            recruitmentRepository.deleteById(savedRecruitment.getId());

            // then
            assertThat(recruitmentRepository.findById(savedRecruitment.getId()).isPresent()).isFalse();
        }
    }
}
