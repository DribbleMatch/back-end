package com.sideProject.DribbleMatch.service.auth;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.JwtTokenProvider;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.common.util.RedisUtil;
import com.sideProject.DribbleMatch.dto.user.request.UserSignInRequest;
import com.sideProject.DribbleMatch.dto.user.response.JwtResonseDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private Region initRegion(String dong) {
        return Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build();
    }

    private User initUser(String email, String name, Region region) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode("test1234!A"))
                .nickName(name)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .region(region)
                .build();
    }

    @Nested
    @DisplayName("userSignInTest")
    public class userSignInTest {

        @DisplayName("로그인을 하고 토큰을 반환한다")
        @Test
        public void userSignIn() {

            // given
            Region region = initRegion("당산동");

            UserSignInRequest request = UserSignInRequest.builder()
                    .email("test@test.com")
                    .password("test1234!A")
                    .build();

            User user = initUser("test@test.com", "test", region);

            // mocking
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(user));
            when(jwtTokenProvider.createAccessToken(user)).thenReturn("testAccessToken");
            when(jwtTokenProvider.createRefreshToken(user)).thenReturn("testRefreshToken");

            // when
            JwtResonseDto response = authService.userSignIn(request);

            // then
            assertThat(response.getAccessToken()).isEqualTo("testAccessToken");
            assertThat(response.getRefreshToken()).isEqualTo("testRefreshToken");
        }

        @DisplayName("없는 이메일이면 에러가 발생한다")
        @Test
        public void userSignIn2() {

            // given
            UserSignInRequest request = UserSignInRequest.builder()
                    .email("test@test.com")
                    .password("test1234!A")
                    .build();

            // mocking
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

            // when, given
            assertThatThrownBy(() -> authService.userSignIn(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 이메일이 존재하지 않습니다.");
        }

        @DisplayName("이메일이 있지만 비밀번호가 틀리면 에러가 발생한다")
        @Test
        public void userSignIn3() {

            // given
            Region region = initRegion("당산동");

            UserSignInRequest request = UserSignInRequest.builder()
                    .email("test@test.com")
                    .password("test1234")
                    .build();

            User user = initUser("test@test.com", "test", region);

            // mocking
            when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(user));

            // when, given
            assertThatThrownBy(() -> authService.userSignIn(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("비밀 번호가 틀렸습니다.");
        }
    }

    @Nested
    @DisplayName("refreshTest")
    public class refreshTest {

        @DisplayName("refreshToken을 받아 refreshToken과 accessToken을 재발급한다")
        @Test
        public void refresh() {
            // given
            Region region = initRegion("당산동");

            User user = initUser("test@test.com", "test", region);

            String pastRefreshToken = "pastRefreshToken";

            // mocking
            doNothing().when(jwtUtil).validateRefreshToken(any(String.class));
            when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
            when(redisUtil.getData("pastRefreshToken")).thenReturn("1");
            when(jwtTokenProvider.createAccessToken(user)).thenReturn("accessToken");
            when(jwtTokenProvider.createRefreshToken(user)).thenReturn("refreshToken");

            // when
            JwtResonseDto result = authService.refresh(pastRefreshToken);

            // then
            assertThat(result.getAccessToken()).isEqualTo("accessToken");
            assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
        }

        @DisplayName("refreshToken의 유효성 검사가 실패하면 예외가 발생한다")
        @Test
        public void refresh2() {

            // given
            String refreshToken = "pastRefreshToken";

            // mocking
            doThrow(new CustomException(ErrorCode.INVALID_REFRESH_TOKEN)).when(jwtUtil).validateRefreshToken(refreshToken);

            // given
            assertThatThrownBy(() -> authService.refresh(refreshToken))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("REFRESH TOKEN 인증 오류");
        }
    }
}
