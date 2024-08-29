//package com.sideProject.DribbleMatch.service.user;
//
//import com.sideProject.DribbleMatch.common.error.CustomException;
//import com.sideProject.DribbleMatch.common.error.ErrorCode;
//import com.sideProject.DribbleMatch.common.util.JwtTokenProvider;
//import com.sideProject.DribbleMatch.common.util.JwtUtil;
//import com.sideProject.DribbleMatch.common.util.RedisUtil;
//import com.sideProject.DribbleMatch.dto.user.response.JwtResonseDto;
//import com.sideProject.DribbleMatch.dto.user.request.UserSignInRequest;
//import com.sideProject.DribbleMatch.dto.user.request.UserSignUpRequestDto;
//import com.sideProject.DribbleMatch.entity.region.Region;
//import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
//import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
//import com.sideProject.DribbleMatch.entity.user.User;
//import com.sideProject.DribbleMatch.repository.region.RegionRepository;
//import com.sideProject.DribbleMatch.repository.user.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Spy
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Mock
//    private RegionRepository regionRepository;
//
//    @Mock
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private RedisUtil redisUtil;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//                   private Region initRegion(String dong) {
//        return Region.builder()
//                .siDo("서울특별시")
//                .siGunGu("영등포구")
//                .eupMyeonDongGu(dong)
//                .latitude(37.5347)
//                .longitude(126.9065)
//                .eupMyeonDongGu("")
//                .eupMyeonLeeDong("")
//                .lee("")
//                .build();
//    }
//
//    private User initUser(String email, String name, Region region) {
//        return User.builder()
//                .email(email)
//                .password(passwordEncoder.encode("test1234!A"))
//                .nickName(name)
//                .gender(Gender.MALE)
//                .birth(LocalDate.of(2001, 1, 1))
//                .position(Position.CENTER)
//                .winning(10)
//                .region(region)
//                .build();
//    }
//
//    @Nested
//    @DisplayName("signUpTest")
//    public class signUpTest {
//
//        @DisplayName("회원가입을 한다")
//        @Test
//        public void signUp() {
//
//            // given
//            Region region = initRegion("당산동");
//
//            User user = initUser("test@test.com", "test", region);
//            Long fakeMemberId = 1L;
//            ReflectionTestUtils.setField(user, "id", fakeMemberId);
//
//            UserSignUpRequestDto request = UserSignUpRequestDto.builder()
//                    .email("test@test.com")
//                    .password("test1234!A")
//                    .rePassword("test1234!A")
//                    .nickName("test")
//                    .gender(Gender.MALE)
//                    .birth(LocalDate.of(2001, 1, 1))
//                    .position(Position.CENTER)
//                    .regionString("서울특별시 영등포구 당산동")
//                    .build();
//
//            // mocking
//            when(userRepository.save(any(User.class))).thenReturn(user);
//            when(regionRepository.findByRegionString("서울특별시 영등포구 당산동")).thenReturn(Optional.ofNullable(region));
//
//            // when
//            Long userId = userService.signUp(request);
//
//            // then
//            assertThat(userId).isEqualTo(user.getId());
//        }
//
//        @DisplayName("이메일이 중복이면 에러가 발생한다")
//        @Test
//        public void signUp2() {
//
//            // given
//            Region region = initRegion("당산동");
//
//            UserSignUpRequestDto request = UserSignUpRequestDto.builder()
//                    .email("test@test.com")
//                    .password("test1234!A")
//                    .rePassword("test1234!A")
//                    .nickName("test")
//                    .gender(Gender.MALE)
//                    .birth(LocalDate.of(2001, 1, 1))
//                    .position(Position.CENTER)
//                    .build();
//
//            User user = initUser("test@test.com", "test", region);
//
//            // mocking
//            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(user));
//
//            // when, then
//            assertThatThrownBy(() -> userService.signUp(request))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("이메일이 이미 존재합니다.");
//        }
//
//        @DisplayName("닉네임이 중복이면 에러가 발생한다")
//        @Test
//        public void signUp3() {
//
//            // given
//            Region region = initRegion("당산동");
//
//            UserSignUpRequestDto request = UserSignUpRequestDto.builder()
//                    .email("test@test.com")
//                    .password("test1234!A")
//                    .rePassword("test1234!A")
//                    .nickName("test")
//                    .gender(Gender.MALE)
//                    .birth(LocalDate.of(2001, 1, 1))
//                    .position(Position.CENTER)
//                    .build();
//
//            User user = initUser("test@test.com", "test", region);
//
//            // mocking
//            when(userRepository.findByNickName(request.getNickName())).thenReturn(Optional.ofNullable(user));
//
//            // when, then
//            assertThatThrownBy(() -> userService.signUp(request))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("닉네임이 이미 존재합니다.");
//        }
//
//        @DisplayName("비밀번호 유효성 검사에 통과하지 못하면 예외가 발생한다")
//        @Test
//        public void signUp4() {
//
//            // given
//            UserSignUpRequestDto request = UserSignUpRequestDto.builder()
//                    .email("test@test.com")
//                    .password("test1234")
//                    .rePassword("test1234")
//                    .nickName("test")
//                    .gender(Gender.MALE)
//                    .birth(LocalDate.of(2001, 1, 1))
//                    .position(Position.CENTER)
//                    .build();
//
//            // when, then
//            assertThatThrownBy(() -> userService.signUp(request))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("비밀번호는 대문자, 소문자, 숫자, 특수 문자가 하나 이상 포함되어야 합니다.");
//        }
//
//        @DisplayName("비밀번호와 비밀번호 재입력이 다르면 에러가 발생한다")
//        @Test
//        public void signUp5() {
//
//            // given
//            UserSignUpRequestDto request = UserSignUpRequestDto.builder()
//                    .email("test@test.com")
//                    .password("test1234!A")
//                    .rePassword("test1234!A!")
//                    .nickName("test")
//                    .gender(Gender.MALE)
//                    .birth(LocalDate.of(2001, 1, 1))
//                    .position(Position.CENTER)
//                    .regionString("서울특별시 영등포구 당산동")
//                    .build();
//
//            // when, then
//            assertThatThrownBy(() -> userService.signUp(request))
//                    .isInstanceOf(CustomException.class)
//                    .hasMessage("비밀번호가 다릅니다.");
//        }
//    }
//}
