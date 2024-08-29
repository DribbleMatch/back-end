//package com.sideProject.DribbleMatch.repository.matching;
//
//import com.sideProject.DribbleMatch.common.error.CustomException;
//import com.sideProject.DribbleMatch.common.error.ErrorCode;
//import com.sideProject.DribbleMatch.config.QuerydslConfig;
//import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
//import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
//import com.sideProject.DribbleMatch.entity.region.Region;
//import com.sideProject.DribbleMatch.entity.team.Team;
//import com.sideProject.DribbleMatch.entity.user.User;
//import com.sideProject.DribbleMatch.repository.matching.personalMatching.PersonalMatchingRepository;
//import com.sideProject.DribbleMatch.repository.region.RegionRepository;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//@Import(QuerydslConfig.class)
//public class PersonalMatchingRepositoryTest {
//
//    @Autowired
//    private RegionRepository regionRepository;
//
//    @Autowired
//    private PersonalMatchingRepository personalMatchingRepository;
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
//    private PersonalMatching initPersonalMatching(String name, Region region) {
//        return PersonalMatching.builder()
//                .name(name)
//                .playPeople(5)
//                .maxPeople(7)
//                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
//                .status(MatchingStatus.RECRUITING)
//                .region(region)
//                .build();
//    }
//    @Nested
//    @DisplayName("CreatePersonalMatchingTest")
//    public class CreatePersonalMatchingTest {
//
//        @DisplayName("PersonalMatching을 생성한다")
//        @Test
//        public void createPersonalMatching() {
//
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            // when
//            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(personalMatching);
//
//            // given
//            assertThat(savedPersonalMatching).isNotNull();
//            assertThat(savedPersonalMatching).isEqualTo(personalMatching);
//        }
//
//        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
//        @Test
//        public void createPersonalMatching2() {
//
//            // given
//            Region region = initRegion("당산동");
//
//            PersonalMatching personalMatching = PersonalMatching.builder()
////                    .name("testPersonalMatching")
//                    .playPeople(5)
//                    .maxPeople(7)
//                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                    .endAt(LocalDateTime.of(2001, 1, 1, 14, 0))
//                    .status(MatchingStatus.RECRUITING)
//                    .region(region)
//                    .build();
//
//            // when, then
//            assertThatThrownBy(() -> personalMatchingRepository.save(personalMatching))
//                    .isInstanceOf(ConstraintViolationException.class);
//        }
//
//        @DisplayName("name이 중복이면 에러가 발생한다")
//        @Test
//        public void createTeam3() {
//
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching personalMatching1 = initPersonalMatching("testPersonalMatching", region);
//            PersonalMatching personalMatching2 = initPersonalMatching("testPersonalMatching", region);
//
//            // when
//            personalMatchingRepository.save(personalMatching1);
//
//            // then
//            assertThatThrownBy(() -> personalMatchingRepository.save(personalMatching2))
//                    .isInstanceOf(DataIntegrityViolationException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("SelectPersonalMatchingTest")
//    public class SelectPersonalMatchingTest {
//
//        @DisplayName("PersonalMatching을 조회한다")
//        @Test
//        public void selectPersonalMatching() {
//
//            // given
//            Region region = initRegion("당산동");
//
//            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching", region));
//
//            // when
//            PersonalMatching selectedPersonalMatching = personalMatchingRepository.findById(savedPersonalMatching.getId()).get();
//
//            // then
//            assertThat(selectedPersonalMatching).isNotNull();
//            assertThat(selectedPersonalMatching).isEqualTo(savedPersonalMatching);
//        }
//
//        @DisplayName("모든 PersonalMatching을 조회한다")
//        @Test
//        public void selectPersonalMatching2() {
//            // given
//            Region region = initRegion("당산동");
//
//            PersonalMatching savedPersonalMatching1 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching1", region));
//            PersonalMatching savedPersonalMatching2 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching2", region));
//
//            // when
//            List<PersonalMatching> personalMatchings = personalMatchingRepository.findAll();
//
//            // then
//            assertThat(personalMatchings.size()).isEqualTo(2);
//            assertThat(personalMatchings.contains(savedPersonalMatching1)).isTrue();
//            assertThat(personalMatchings.contains(savedPersonalMatching2)).isTrue();
//        }
//
//        @DisplayName("없는 PersonalMatching이면 에러가 발생한다")
//        @Test
//        public void selectPersonalMatching3() {
//            // given
//            Region region = initRegion("당산동");
//
//            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching", region));
//
//            // when, then
//            assertThatThrownBy(() -> personalMatchingRepository.findById(savedPersonalMatching.getId() + 1).orElseThrow(() ->
//                    new CustomException(ErrorCode.NOT_FOUND_PERSONAL_MATCHING_ID)))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("해당 개인 경기가 존재하지 않습니다.");
//        }
//
//        @DisplayName("Name으로 PersonalMatching을 조회한다")
//        @Test
//        public void selectPersonalMatching4() {
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching1", region));
//
//            // when
//            PersonalMatching selectedPersonalMatching = personalMatchingRepository.findByName("testPersonalMatching1").get();
//
//            // then
//            assertThat(selectedPersonalMatching).isNotNull();
//            assertThat(selectedPersonalMatching).isEqualTo(savedPersonalMatching);
//        }
//    }
//
//    @Nested
//    @DisplayName("UpdateTeamTest")
//    public class UpdateTeamTest {
//
//        @DisplayName("팀 정보를 수정한다")
//        @Test
//        public void updateTeam() {
//
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching", region));
//
//            Region newRegion = initRegion("문래동");
//
//            // when
//            ReflectionTestUtils.setField(savedPersonalMatching, "name", "testPersonalMatching");
//            ReflectionTestUtils.setField(savedPersonalMatching, "playPeople", 3);
//
//
//            PersonalMatching updatedPersonalMatching = personalMatchingRepository.findById(savedPersonalMatching.getId()).get();
//
//            // then
//            assertThat(updatedPersonalMatching.getName()).isEqualTo("testPersonalMatching");
//            assertThat(updatedPersonalMatching.getPlayPeople()).isEqualTo(3);
//        }
//    }
//
//    @Nested
//    @DisplayName("DeletePersonalMatchingTest")
//    public class DeletePersonalMatchingTest {
//
//        @DisplayName("PersonalMatching을 삭제한다")
//        @Test
//        public void deletePersonalMatching() {
//
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching savedPersonalMatching = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching", region));
//
//            // when
//            personalMatchingRepository.deleteById(savedPersonalMatching.getId());
//
//            // then
//            assertThat(personalMatchingRepository.findById(savedPersonalMatching.getId()).isPresent()).isFalse();
//        }
//    }
//
//    @DisplayName("Region Id의 배열에 포함되는 Region을 가진 PersonalMatching을 조회한다")
//    @Test
//    public void findByRegionIds() {
//
//        // given
//        Region region1 = initRegion("당산동");
//        Region region2 = initRegion("문래동");
//
//        PersonalMatching personalMatching1 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching1", region1));
//        PersonalMatching personalMatching2 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching2", region1));
//        PersonalMatching personalMatching3 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching3", region2));
//        PersonalMatching personalMatching4 = personalMatchingRepository.save(initPersonalMatching("testPersonalMatching4", region2));
//
//        Pageable pageable = PageRequest.of(0, 2);
//
//        // when
//        Page<PersonalMatching> personalMatchings1 = personalMatchingRepository.findByRegionIds(pageable, List.of(region1.getId()));
//        Page<PersonalMatching> personalMatchings2 = personalMatchingRepository.findByRegionIds(pageable, List.of(region2.getId()));
//        Page<PersonalMatching> personalMatchings3 = personalMatchingRepository.findByRegionIds(pageable, List.of(region1.getId(), region2.getId()));
//
//        // then
//        assertThat(personalMatchings1.getContent().size()).isEqualTo(2);
//        assertThat(personalMatchings1.getTotalPages()).isEqualTo(1);
//        assertThat(personalMatchings1.getTotalElements()).isEqualTo(2);
//        assertThat(personalMatchings1.get().toList().get(0)).isEqualTo(personalMatching1);
//        assertThat(personalMatchings1.get().toList().get(1)).isEqualTo(personalMatching2);
//
//        assertThat(personalMatchings2.getContent().size()).isEqualTo(2);
//        assertThat(personalMatchings2.getTotalPages()).isEqualTo(1);
//        assertThat(personalMatchings2.getTotalElements()).isEqualTo(2);
//        assertThat(personalMatchings2.get().toList().get(0)).isEqualTo(personalMatching3);
//        assertThat(personalMatchings2.get().toList().get(1)).isEqualTo(personalMatching4);
//
//        assertThat(personalMatchings3.getContent().size()).isEqualTo(2);
//        assertThat(personalMatchings3.getTotalPages()).isEqualTo(2);
//        assertThat(personalMatchings3.getTotalElements()).isEqualTo(4);
//        assertThat(personalMatchings3.get().toList().get(0)).isEqualTo(personalMatching1);
//        assertThat(personalMatchings3.get().toList().get(1)).isEqualTo(personalMatching2);
//    }
//}
