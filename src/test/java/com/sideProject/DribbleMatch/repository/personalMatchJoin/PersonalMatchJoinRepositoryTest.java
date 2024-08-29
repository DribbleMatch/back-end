//package com.sideProject.DribbleMatch.repository.personalMatchJoin;
//
//import com.sideProject.DribbleMatch.common.error.CustomException;
//import com.sideProject.DribbleMatch.common.error.ErrorCode;
//import com.sideProject.DribbleMatch.config.QuerydslConfig;
//import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
//import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
//import com.sideProject.DribbleMatch.entity.region.Region;
//import com.sideProject.DribbleMatch.repository.matching.personalMatching.PersonalMatchingRepository;
//import com.sideProject.DribbleMatch.entity.personalMatchJoin.PersonalMatchJoin;
//import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
//import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
//import com.sideProject.DribbleMatch.entity.user.User;
//import com.sideProject.DribbleMatch.repository.region.RegionRepository;
//import com.sideProject.DribbleMatch.repository.user.UserRepository;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//@Import(QuerydslConfig.class)
//public class PersonalMatchJoinRepositoryTest {
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PersonalMatchingRepository personalMatchingRepository;
//
//    @Autowired
//    private PersonalMatchJoinRepository personalMatchJoinRepository;
//
//    private Region initRegion(String dong) {
//        return regionRepository.save(Region.builder()
//                .siDo("서울특별시")
//                .siGunGu("영등포구")
//                .eupMyeonDongGu(dong)
//                .latitude(37.5347)
//                .longitude(126.9065)
//                .build());
//    }
//
//    private User initUser(String email, String name, Region region) {
//        return userRepository.save(User.builder()
//                .email(email)
//                .password("test1234!A")
//                .nickName(name)
//                .gender(Gender.MALE)
//                .birth(LocalDate.of(2001, 1, 1))
//                .position(Position.CENTER)
//                .winning(10)
//                .region(region)
//                .build());
//    }
//
//    private PersonalMatching initPersonalMatching(String name, Region region) {
//        return personalMatchingRepository.save(PersonalMatching.builder()
//                .name(name)
//                .playPeople(5)
//                .maxPeople(7)
//                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
//                .status(MatchingStatus.RECRUITING)
//                .region(region)
//                .build());
//    }
//
//    private PersonalMatchJoin initPersonalMatchJoin(User user, PersonalMatching personalMatching) {
//        return PersonalMatchJoin.builder()
//                .user(user)
//                .personalMatching(personalMatching)
//                .build();
//    }
//
//    @Nested
//    @DisplayName("CreatePersonalMatchJoinTest")
//    public class CreatePersonalMatchJoinTest {
//
//        @DisplayName("PersonalMatchJoin를 생성한다")
//        @Test
//        public void createPersonalMatchJoin() {
//
//            // given
//            Region region = initRegion("당산동");
//            User user = initUser("test@test.com", "test", region);
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            PersonalMatchJoin personalMatchJoin = initPersonalMatchJoin(user, personalMatching);
//
//            // when
//            PersonalMatchJoin savedPersonal = personalMatchJoinRepository.save(personalMatchJoin);
//
//            // then
//            assertThat(savedPersonal).isNotNull();
//            assertThat(savedPersonal).isEqualTo(personalMatchJoin);
//        }
//
//        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
//        @Test
//        public void createPersonalMatchJoin2() {
//
//            // given
//            Region region = initRegion("당산동");
//            User user = initUser("test@test.com", "test", region);
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            PersonalMatchJoin personalMatchJoin = initPersonalMatchJoin(user, null);
//
//            // when, then
//            assertThatThrownBy(() -> personalMatchJoinRepository.save(personalMatchJoin))
//                    .isInstanceOf(ConstraintViolationException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("SelectPersonalMatchJoinTest")
//    public class SelectPersonalMatchJoinTest {
//
//        @DisplayName("PersonalMatchJoin를 조회한다")
//        @Test
//        public void selectPersonalMatchJoin() {
//
//            // given
//            Region region = initRegion("당산동");
//            User user = initUser("test@test.com", "test", region);
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            PersonalMatchJoin savedPersonalMatchJoin = personalMatchJoinRepository.save(initPersonalMatchJoin(user, personalMatching));
//
//            // when
//            PersonalMatchJoin selectedPersonalMatchJoin = personalMatchJoinRepository.findById(savedPersonalMatchJoin.getId()).get();
//
//            // then
//            assertThat(selectedPersonalMatchJoin).isNotNull();
//            assertThat(selectedPersonalMatchJoin).isEqualTo(savedPersonalMatchJoin);
//        }
//
//        @DisplayName("모든 PersonalMatchJoin를 조회한다")
//        @Test
//        public void selectPersonalMatchJoin2() {
//
//            // given
//            Region region = initRegion("당산동");
//            User user = initUser("test@test.com", "test", region);
//            PersonalMatching personalMatching1 = initPersonalMatching("testPersonalMatching1", region);
//            PersonalMatching personalMatching2 = initPersonalMatching("testPersonalMatching2", region);
//
//            PersonalMatchJoin savedPersonalMatchJoin1 = personalMatchJoinRepository.save(initPersonalMatchJoin(user, personalMatching1));
//            PersonalMatchJoin savedPersonalMatchJoin2 = personalMatchJoinRepository.save(initPersonalMatchJoin(user, personalMatching2));
//
//            // when
//            List<PersonalMatchJoin> personalMatchJoins = personalMatchJoinRepository.findAll();
//
//            // then
//            assertThat(personalMatchJoins.size()).isEqualTo(2);
//            assertThat(personalMatchJoins.contains(savedPersonalMatchJoin1)).isTrue();
//            assertThat(personalMatchJoins.contains(savedPersonalMatchJoin2)).isTrue();
//        }
//
//        @DisplayName("없는 PersonalMatchJoin이면 에러가 발생한다")
//        @Test
//        public void selectPersonalMatchJoin3() {
//
//            // given
//            Region region = initRegion("당산동");
//            User user = initUser("test@test.com", "test", region);
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            PersonalMatchJoin savedPersonalMatchJoin = personalMatchJoinRepository.save(initPersonalMatchJoin(user, personalMatching));
//
//            // when, then
//            assertThatThrownBy(() -> personalMatchJoinRepository.findById(savedPersonalMatchJoin.getId() + 1).orElseThrow(() ->
//                    new CustomException(ErrorCode.NOT_FOUND_PERSONAL_MATCH_JOIN_ID)))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("해당 개인 경기 참가 정보가 존재하지 않습니다.");
//        }
//    }
//
//    @Nested
//    @DisplayName("DeletePersonalMatchJoinTest")
//    public class DeletePersonalMatchJoinTest {
//
//        @DisplayName("PersonalMatchJoin를 삭제한다")
//        @Test
//        public void deletePersonalMatchJoin() {
//
//            // given
//            Region region = initRegion("당산동");
//            User user = initUser("test@test.com", "test", region);
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            PersonalMatchJoin savedPersonalMatchJoin = personalMatchJoinRepository.save(initPersonalMatchJoin(user, personalMatching));
//
//            // when
//            personalMatchJoinRepository.deleteById(savedPersonalMatchJoin.getId());
//
//            // then
//            assertThat(personalMatchJoinRepository.findById(savedPersonalMatchJoin.getId()).isPresent()).isFalse();
//        }
//    }
//}
