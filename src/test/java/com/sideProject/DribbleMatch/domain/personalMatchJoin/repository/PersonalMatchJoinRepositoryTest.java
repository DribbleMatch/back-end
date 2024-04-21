package com.sideProject.DribbleMatch.domain.personalMatchJoin.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingType;
import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import com.sideProject.DribbleMatch.domain.matching.repository.MatchingRepository;
import com.sideProject.DribbleMatch.domain.personalMatchJoin.entity.PersonalMatchJoin;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import com.sideProject.DribbleMatch.domain.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PersonalMatchJoinRepositoryTest {

    @Autowired
    PersonalMatchJoinRepository personalMatchJoinRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchingRepository matchingRepository;

    private User initUser(String email) {
        return userRepository.save(User.builder()
                .email(email)
                .password("test1234")
                .nickName("test")
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .build());
    }

    private Matching initMatching(String name) {
        return matchingRepository.save(Matching.builder()
                .name(name)
                .people(10)
                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
                .place("서울")
                .status(MatchingStatus.RECRUITING)
                .type(MatchingType.TEAM)
                .build());
    }

    private PersonalMatchJoin initPersonalMatchJoin(User user, Matching matching) {
        return PersonalMatchJoin.builder()
                .user(user)
                .matching(matching)
                .build();
    }

    @Nested
    @DisplayName("createPersonalMatchJoinTest")
    public class createPersonalMatchJoinTest {

        @DisplayName("PersonalMatchJoin를 생성한다")
        @Test
        public void createPersonalMatchJoin() {

            // given
            User user = initUser("test@test.com");
            Matching matching = initMatching("testMatching");
            PersonalMatchJoin personalMatchJoin = initPersonalMatchJoin(user, matching);

            // when
            PersonalMatchJoin savedPersonal = personalMatchJoinRepository.save(personalMatchJoin);

            // then
            assertThat(savedPersonal).isNotNull();
            assertThat(savedPersonal).isEqualTo(personalMatchJoin);
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createPersonalMatchJoin2() {

            // given
            User user = initUser("test@test.com");
            Matching matching = initMatching("testMatching");
            PersonalMatchJoin personalMatchJoin = initPersonalMatchJoin(user, null);

            // when, then
            assertThatThrownBy(() -> personalMatchJoinRepository.save(personalMatchJoin))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("경기가 입력되지 않았습니다.");
        }
    }

    @Nested
    @DisplayName("selectPersonalMatchJoinTest")
    public class selectPersonalMatchJoinTest {

        @DisplayName("PersonalMatchJoin를 조회한다")
        @Test
        public void selectPersonalMatchJoin() {

            // given
            PersonalMatchJoin savedPersonalMatchJoin = personalMatchJoinRepository.save(initPersonalMatchJoin(initUser("test@test.com"), initMatching("testMatching")));

            // when
            PersonalMatchJoin selectedPersonalMatchJoin = personalMatchJoinRepository.findById(savedPersonalMatchJoin.getId()).get();

            // then
            assertThat(selectedPersonalMatchJoin).isNotNull();
            assertThat(selectedPersonalMatchJoin).isEqualTo(savedPersonalMatchJoin);
        }

        @DisplayName("모든 PersonalMatchJoin를 조회한다")
        @Test
        public void selectPersonalMatchJoin2() {

            // given
            PersonalMatchJoin savedPersonalMatchJoin1 = personalMatchJoinRepository.save(initPersonalMatchJoin(initUser("test@test.com1"), initMatching("testMatching1")));
            PersonalMatchJoin savedPersonalMatchJoin2 = personalMatchJoinRepository.save(initPersonalMatchJoin(initUser("test@test.com2"), initMatching("testMatching2")));

            // when
            List<PersonalMatchJoin> personalMatchJoins = personalMatchJoinRepository.findAll();

            // then
            assertThat(personalMatchJoins.size()).isEqualTo(2);
            assertThat(personalMatchJoins.contains(savedPersonalMatchJoin1)).isTrue();
            assertThat(personalMatchJoins.contains(savedPersonalMatchJoin2)).isTrue();
        }

        @DisplayName("없는 PersonalMatchJoin이면 에러가 발생한다")
        @Test
        public void selectPersonalMatchJoin3() {

            // given
            PersonalMatchJoin savedPersonalMatchJoin = personalMatchJoinRepository.save(initPersonalMatchJoin(initUser("test@test.com"), initMatching("testMatching")));

            // when, then
            assertThatThrownBy(() -> personalMatchJoinRepository.findById(savedPersonalMatchJoin.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_PERSONAL_MATCH_JOIN_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 개인 경기 참가 정보가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deletePersonalMatchJoinTest")
    public class deletePersonalMatchJoinTest {

        @DisplayName("PersonalMatchJoin를 삭제한다")
        @Test
        public void deletePersonalMatchJoin() {

            // given
            PersonalMatchJoin savedPersonalMatchJoin = personalMatchJoinRepository.save(initPersonalMatchJoin(initUser("test@test.com"), initMatching("testMatching")));

            // when
            personalMatchJoinRepository.deleteById(savedPersonalMatchJoin.getId());

            // then
            assertThat(personalMatchJoinRepository.findById(savedPersonalMatchJoin.getId()).isPresent()).isFalse();
        }
    }
}
