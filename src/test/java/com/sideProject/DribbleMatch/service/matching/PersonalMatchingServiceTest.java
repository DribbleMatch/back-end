//package com.sideProject.DribbleMatch.service.matching;
//
//import com.sideProject.DribbleMatch.common.error.CustomException;
//import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
//import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
//import com.sideProject.DribbleMatch.dto.matching.response.MatchingResponseDto;
//import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
//import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
//import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
//import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
//import com.sideProject.DribbleMatch.entity.region.Region;
//import com.sideProject.DribbleMatch.entity.team.Team;
//import com.sideProject.DribbleMatch.entity.user.User;
//import com.sideProject.DribbleMatch.repository.matching.personalMatching.PersonalMatchingRepository;
//import com.sideProject.DribbleMatch.repository.region.RegionRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class PersonalMatchingServiceTest {
//
//    @Mock
//    private RegionRepository regionRepository;
//
//    @Mock
//    private PersonalMatchingRepository personalMatchingRepository;
//
//    @InjectMocks
//    private PersonalMatchingServiceImpl personalMatchingService;
//
//    private Region initRegion(String dong) {
//        return Region.builder()
//                .siDo("서울특별시")
//                .siGunGu("영등포구")
//                .eupMyeonDongGu(dong)
//                .latitude(37.5347)
//                .longitude(126.9065)
//                .build();
//    }
//
//    private PersonalMatching initPersonalMatching(String name, Region region) {
//        return PersonalMatching.builder()
//                .name(name)
//                .playPeople(5)
//                .maxPeople(10)
//                .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                .endAt(LocalDateTime.of(2001, 1, 1, 13, 0))
//                .region(region)
//                .build();
//    }
//
//    private Page<PersonalMatching> convertToPage(List<PersonalMatching> personalMatchings, Pageable pageable) {
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), personalMatchings.size());
//        return new PageImpl<>(personalMatchings.subList(start, end), pageable, personalMatchings.size());
//    }
//
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
//            MatchingCreateRequestDto requestDto = MatchingCreateRequestDto.builder()
//                    .name("testPersonalMatching")
//                    .playPeople(5)
//                    .maxPeople(10)
//                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                    .endAt(LocalDateTime.of(2001, 1, 1, 13, 0))
//                    .regionString("서울특별시 영등포구 당산동")
//                    .build();
//
//            // mocking
//            when(regionRepository.findByRegionString("서울특별시 영등포구 당산동")).thenReturn(Optional.ofNullable(region));
//
//            Long fakePersonalMatchingId = 1L;
//            ReflectionTestUtils.setField(personalMatching, "id", fakePersonalMatchingId);
//            when(personalMatchingRepository.save(any(PersonalMatching.class))).thenReturn(personalMatching);
//
//            // when
//            Long personalMatchingId = personalMatchingService.createMatching(requestDto);
//
//            // then
//            assertThat(personalMatchingId).isEqualTo(fakePersonalMatchingId);
//        }
//
//        @DisplayName("name이 중복이면 에러가 발생한다")
//        @Test
//        public void createPersonalMatching2() {
//
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            MatchingCreateRequestDto requestDto = MatchingCreateRequestDto.builder()
//                    .name("testPersonalMatching")
//                    .playPeople(5)
//                    .maxPeople(10)
//                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                    .endAt(LocalDateTime.of(2001, 1, 1, 13, 0))
//                    .regionString("서울특별시 영등포구 당산동")
//                    .build();
//
//            // mocking
//            when(personalMatchingRepository.findByName("testPersonalMatching")).thenReturn(Optional.ofNullable(personalMatching));
//
//            // when
//            assertThatThrownBy(() -> personalMatchingService.createMatching(requestDto))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("개인 경기 이름이 이미 존재합니다.");
//        }
//    }
//
//    @Nested
//    @DisplayName("UpdatePersonalMatching2")
//    public class UpdatePersonalMatching2 {
//
//        @DisplayName("PersonalMatching를 수정한다")
//        @Test
//        public void updatePersonalMatching() {
//
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            Region newRegion = initRegion("문래동");
//
//            MatchingUpdateRequestDto requestDto = MatchingUpdateRequestDto.builder()
//                    .name("testPersonalMatching")
//                    .playPeople(5)
//                    .maxPeople(10)
//                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                    .endAt(LocalDateTime.of(2001, 1, 1, 13, 0))
//                    .regionString("서울특별시 영등포구 문래동")
//                    .build();
//
//
//            // mocking
//            Long fakePersonalMatchingId = 1L;
//            ReflectionTestUtils.setField(personalMatching, "id", fakePersonalMatchingId);
//            when(personalMatchingRepository.findById(fakePersonalMatchingId)).thenReturn(Optional.ofNullable(personalMatching));
//
//            when(regionRepository.findByRegionString("서울특별시 영등포구 문래동")).thenReturn(Optional.ofNullable(newRegion));
//
//            // when
//            Long personalMatchingId =  personalMatchingService.updateMatching(fakePersonalMatchingId, requestDto);
//
//            // then
//            assertThat(personalMatchingId).isEqualTo(fakePersonalMatchingId);
//        }
//
//        @DisplayName("name이 중복이면 에러가 발생한다")
//        @Test
//        public void updatePersonalMatching2() {
//
//            // given
//            Region region = initRegion("당산동");
//
//            Region newRegion = initRegion("문래동");
//
//            MatchingUpdateRequestDto requestDto = MatchingUpdateRequestDto.builder()
//                    .name("testPersonalMatching")
//                    .playPeople(5)
//                    .maxPeople(10)
//                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                    .endAt(LocalDateTime.of(2001, 1, 1, 13, 0))
//                    .regionString("서울특별시 영등포구 문래동")
//                    .build();
//
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            // mocking
//            when(personalMatchingRepository.findByName("testPersonalMatching")).thenReturn(Optional.ofNullable(personalMatching));
//
//            // when, then
//            assertThatThrownBy(() -> personalMatchingService.updateMatching(personalMatching.getId(), requestDto))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("개인 경기 이름이 이미 존재합니다.");
//        }
//
//        @DisplayName("없는 PersonalMatching이면 에러가 발생한다")
//        @Test
//        public void updatePersonalMatching3() {
//
//            // given
//            Region region = initRegion("당산동");
//
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            MatchingUpdateRequestDto requestDto = MatchingUpdateRequestDto.builder()
//                    .name("testPersonalMatching")
//                    .playPeople(5)
//                    .maxPeople(10)
//                    .startAt(LocalDateTime.of(2001, 1, 1, 12, 0))
//                    .endAt(LocalDateTime.of(2001, 1, 1, 13, 0))
//                    .regionString("서울특별시 영등포구 문래동")
//                    .build();
//
//            // mocking
//            when(regionRepository.findByRegionString("서울특별시 영등포구 문래동")).thenReturn(Optional.ofNullable(region));
//
//            Long fakePersonalMatchingId = 1L;
//            ReflectionTestUtils.setField(personalMatching, "id", fakePersonalMatchingId);
//            when(personalMatchingRepository.findByName("testPersonalMatching")).thenReturn(Optional.empty());
//            when(personalMatchingRepository.findById(fakePersonalMatchingId)).thenReturn(Optional.empty());
//
//            // when, then
//            assertThatThrownBy(() -> personalMatchingService.updateMatching(fakePersonalMatchingId, requestDto))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("해당 개인 경기가 존재하지 않습니다.");
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
//            Long personalMatchingId = 1L;
//
//            // mocking
//            doNothing().when(personalMatchingRepository).deleteById(personalMatchingId);
//
//            // when
//            String result = personalMatchingService.deleteMatching(personalMatchingId);
//
//            // then
//            assertThat(result).isEqualTo("개인 경기가 삭제되었습니다.");
//        }
//    }
//
//    @Nested
//    @DisplayName("findAllPersonalMatchingsTest")
//    public class findAllPersonalMatchingsTest {
//
//        @DisplayName("PersonalMatching을 전체 조회한다")
//        @Test
//        public void findAllPersonalMatchings() {
//
//            // given
//            Region region1 = initRegion("당산동");
//            Region region2 = initRegion("문래동");
//            ReflectionTestUtils.setField(region1, "id", 1L);
//            ReflectionTestUtils.setField(region2, "id", 2L);
//
//            PersonalMatching personalMatching1 = initPersonalMatching("testPersonalMatching1", region1);
//            PersonalMatching personalMatching2 = initPersonalMatching("testPersonalMatching2", region2);
//            PersonalMatching personalMatching3 = initPersonalMatching("testPersonalMatching3", region1);
//            PersonalMatching personalMatching4 = initPersonalMatching("testPersonalMatching4", region2);
//
//            List<PersonalMatching> personalMatchings = List.of(personalMatching1, personalMatching2, personalMatching3, personalMatching4);
//            Pageable pageable = PageRequest.of(1, 2);
//            Page<PersonalMatching> personalMatchingsPage = convertToPage(personalMatchings, pageable);
//
//            // mocking
//            when(regionRepository.findRegionStringById(1L)).thenReturn(Optional.ofNullable("서울특별시 영등포구 당산동"));
//            when(regionRepository.findRegionStringById(2L)).thenReturn(Optional.ofNullable("서울특별시 영등포구 문래동"));
//
//            when(personalMatchingRepository.findAll(pageable)).thenReturn(personalMatchingsPage);
//
//            // when
//            Page<MatchingResponseDto> result = personalMatchingService.findAllMatchings(pageable, "ALL");
//
//            // then
//            assertThat(result.getTotalElements()).isEqualTo(4);
//            assertThat(result.getContent().size()).isEqualTo(2);
//            assertThat(result.getTotalPages()).isEqualTo(2);
//
//            assertThat(result.get().toList().get(0).getName()).isEqualTo("testPersonalMatching3");
//            assertThat(result.get().toList().get(1).getName()).isEqualTo("testPersonalMatching4");
//        }
//
//        @DisplayName("PersonalMatching을 지역별로 조회한다")
//        @Test
//        public void findAllPersonalMatchings2() {
//
//            // given
//            String requestRegionString1 = "서울특별시 영등포구";
//            String requestRegionString2 = "서울특별시 영등포구 당산동";
//            String requestRegionString3 = "서울특별시 영등포구 문래동";
//            String requestRegionString4 = "서울특별시 마포구 합정동";
//
//            Region region1 = initRegion(null);
//            Region region2 = initRegion("당산동");
//            Region region3 = initRegion("문래동");
//            Region region4 = initRegion("합정동");
//            ReflectionTestUtils.setField(region1, "id", 1L);
//            ReflectionTestUtils.setField(region2, "id", 2L);
//            ReflectionTestUtils.setField(region3, "id", 3L);
//            ReflectionTestUtils.setField(region4, "id", 4L);
//
//            PersonalMatching personalMatching1 = initPersonalMatching("testPersonalMatching1", region1);
//            PersonalMatching personalMatching2 = initPersonalMatching("testPersonalMatching2", region2);
//            PersonalMatching personalMatching3 = initPersonalMatching("testPersonalMatching3", region3);
//            PersonalMatching personalMatching4 = initPersonalMatching("testPersonalMatching4", region4);
//
//            Pageable pageable = PageRequest.of(0, 2);
//
//            // mocking
//            when(regionRepository.findIdsByRegionString("서울특별시 영등포구")).thenReturn(List.of(1L, 2L, 3L));
//            when(regionRepository.findIdsByRegionString("서울특별시 영등포구 당산동")).thenReturn(List.of(2L));
//            when(regionRepository.findIdsByRegionString("서울특별시 마포구 합정동")).thenReturn(List.of(4L));
//            when(regionRepository.findRegionStringById(1L)).thenReturn(Optional.ofNullable(requestRegionString1));
//            when(regionRepository.findRegionStringById(2L)).thenReturn(Optional.ofNullable(requestRegionString2));
//            when(regionRepository.findRegionStringById(4L)).thenReturn(Optional.ofNullable(requestRegionString4));
//
//            Page<PersonalMatching> personalMatchingsPage1 = convertToPage(List.of(personalMatching1, personalMatching2, personalMatching4), pageable);
//            Page<PersonalMatching> personalMatchingsPage2 = convertToPage(List.of(personalMatching2), pageable);
//            Page<PersonalMatching> personalMatchingsPage3 = convertToPage(List.of(personalMatching4), pageable);
//
//            when(personalMatchingRepository.findByRegionIds(pageable, List.of(1L, 2L, 3L))).thenReturn(personalMatchingsPage1);
//            when(personalMatchingRepository.findByRegionIds(pageable, List.of(2L))).thenReturn(personalMatchingsPage2);
//            when(personalMatchingRepository.findByRegionIds(pageable, List.of(4L))).thenReturn(personalMatchingsPage3);
//
//            // when
//            Page<MatchingResponseDto> result1 = personalMatchingService.findAllMatchings(pageable, requestRegionString1);
//            Page<MatchingResponseDto> result2 = personalMatchingService.findAllMatchings(pageable, requestRegionString2);
//            Page<MatchingResponseDto> result3 = personalMatchingService.findAllMatchings(pageable, requestRegionString4);
//
//            // then
//            assertThat(result1.getTotalPages()).isEqualTo(2);
//            assertThat(result1.getTotalElements()).isEqualTo(3);
//            assertThat(result1.getContent().size()).isEqualTo(2);
//            assertThat(result1.get().toList().get(0).getName()).isEqualTo("testPersonalMatching1");
//            assertThat(result1.get().toList().get(1).getName()).isEqualTo("testPersonalMatching2");
//
//            assertThat(result2.getTotalPages()).isEqualTo(1);
//            assertThat(result2.getTotalElements()).isEqualTo(1);
//            assertThat(result2.getContent().size()).isEqualTo(1);
//            assertThat(result2.get().toList().get(0).getName()).isEqualTo("testPersonalMatching2");
//
//            assertThat(result3.getTotalPages()).isEqualTo(1);
//            assertThat(result3.getTotalElements()).isEqualTo(1);
//            assertThat(result3.getContent().size()).isEqualTo(1);
//            assertThat(result3.get().toList().get(0).getName()).isEqualTo("testPersonalMatching4");
//        }
//    }
//
//    @Nested
//    @DisplayName("findPersonalMatchingTest")
//    public class findPersonalMatchingTest {
//
//        @DisplayName("개인 매칭을 상세 조회한다")
//        @Test
//        public void findPersonalMatching() {
//
//            // given
//            Region region = initRegion("당산동");
//            PersonalMatching personalMatching = initPersonalMatching("testPersonalMatching", region);
//
//            // mocking
//            Long fakePersonalMatchingId = 1L;
//            ReflectionTestUtils.setField(personalMatching, "id", fakePersonalMatchingId);
//            when(personalMatchingRepository.findById(fakePersonalMatchingId)).thenReturn(Optional.ofNullable(personalMatching));
//
//            Long fakeRegionId = 1L;
//            ReflectionTestUtils.setField(region, "id", fakeRegionId);
//            when(regionRepository.findRegionStringById(fakeRegionId)).thenReturn(Optional.ofNullable("서울특별시 영등포구 당산동"));
//
//            // when
//            MatchingResponseDto foundPersonalMatching = personalMatchingService.findMatching(fakePersonalMatchingId);
//
//            // then
//            assertThat(foundPersonalMatching.getName()).isEqualTo(personalMatching.getName());
//            assertThat(foundPersonalMatching.getRegionString()).isEqualTo("서울특별시 영등포구 당산동");
//        }
//
//        @DisplayName("존재하지 않는 개인 매칭이면 에러가 발생한다")
//        @Test
//        public void findPersonalMatching2() {
//
//            // mocking
//            Long fakePersonalMatchingId = 1L;
//            when(personalMatchingRepository.findById(fakePersonalMatchingId)).thenReturn(Optional.empty());
//
//            // when, then
//            assertThatThrownBy(() -> personalMatchingService.findMatching(fakePersonalMatchingId))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("해당 개인 경기가 존재하지 않습니다.");
//        }
//    }
//}
