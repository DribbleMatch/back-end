package com.sideProject.DribbleMatch.controller.user;

import com.sideProject.DribbleMatch.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.config.SecurityConfig;
import com.sideProject.DribbleMatch.controller.user.UserController;
import com.sideProject.DribbleMatch.dto.user.response.JwtResonseDto;
import com.sideProject.DribbleMatch.dto.user.request.UserSignInRequest;
import com.sideProject.DribbleMatch.dto.user.request.UserSignUpRequestDto;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.auth.AuthService;
import com.sideProject.DribbleMatch.service.user.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//: security 미동작
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {UserController.class, SecurityConfig.class})
@ActiveProfiles("test")
public class UserControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private RegionRepository regionRepository;

    @SpyBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("UserSignUpTest")
    public class UserSignUpTest {
        @DisplayName("회원가입 API를 호출한다")
        @Test
        public void userSignUp() throws Exception {

            // given
            String request = "{\"email\": \"test@test.com\", " +
                    "\"password\": \"test1234!A\", " +
                    "\"rePassword\" : \"test1234!A\", " +
                    "\"nickName\" : \"test\", " +
                    "\"gender\" : \"MALE\", " +
                    "\"birth\" : \"2001-01-01\", " +
                    "\"position\" : \"CENTER\", " +
                    "\"regionString\" : \"서울특별시 영등포구 당산동\"}";

            // mocking
            when(userService.signUp(any(UserSignUpRequestDto.class))).thenReturn(1L);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(1L));
        }

        @DisplayName("NotNull인 컬럼이 Null이면 에러가 발생한다")
        @Test
        public void userSignUp2() throws Exception {

            // given
            String request = "{\"email\": \"test@test.com\", " +
                    "\"rePassword\" : \"test1234!A\", " +
                    "\"nickName\" : \"test\", " +
                    "\"gender\" : \"MALE\", " +
                    "\"birth\" : \"2001-01-01\", " +
                    "\"position\" : \"CENTER\"}";

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Matchers.containsString("비밀번호가 입력되지 않았습니다.")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Matchers.containsString("지역이 입력되지 않았습니다.")));
            ;
        }

        @DisplayName("데이터 형식이나 타입이 잘못되면 에러가 발생한다")
        @Test
        public void userSignUp3() throws Exception {

            // given
            String request = "{\"email\": \"test@test.com\", " +
                    "\"password\": \"test1234!A\", " +
                    "\"rePassword\" : \"test1234!A\", " +
                    "\"nickName\" : \"test\", " +
                    "\"gender\" : \"MALE\", " +
                    "\"birth\" : \"test\", " + // Type에 맞지 않는 값
                    "\"position\" : \"CENTER\", " +
                    "\"regionString\" : \"서울특별시 영등포구 당산동\"}";

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("입력 값의 형식이 맞지 않습니다."));
            ;
        }
    }

    @Nested
    @DisplayName("UserSignInTest")
    public class UserSignInTest {

        @DisplayName("로그인을 하고 토큰을 받는다")
        @Test
        public void userSignIn() throws Exception {

            // given
            String request = "{\"email\": \"test@test.com\", " +
                    "\"password\": \"test@test.com\"}";

            JwtResonseDto response = JwtResonseDto.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .build();

            // mocking
            when(authService.userSignIn(any(UserSignInRequest.class))).thenReturn(response);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").value("accessToken"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").value("accessToken"));
        }

        @DisplayName("NotNull인 컬럼이 Null이면 에러가 발생한다")
        @Test
        public void userSignIn2() throws Exception {

            // given
            String request = "{\"email\": \"test@test.com\"}";

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signIn")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[비밀번호가 입력되지 않았습니다.]"));
        }
    }

    @Nested
    @DisplayName("RefreshTest")
    public class RefreshTest {

        @DisplayName("Header에 있는 Refresh Token으로 토큰 재발급 API를 요청한다")
        @Test
        public void refresh() throws Exception {
            // given
            String refreshToken = "Bearer refreshToken";

            // mocking
            when(authService.refresh("refreshToken")).thenReturn(JwtResonseDto.builder()
                            .accessToken("newAccessToken")
                            .refreshToken("newRefreshToken")
                    .build());
            when(jwtUtil.getTokenTypeFromToken(any(String.class))).thenReturn("REFRESH");

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/refresh")
                            .header("Authorization", refreshToken))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").value("newAccessToken"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.refreshToken").value("newRefreshToken"));
        }

        @DisplayName("Refresh Token이 없으면 에러가 발생한다")
        @Test
        public void refresh2() throws Exception {

            // mocking
            when(authService.refresh("refreshToken")).thenReturn(JwtResonseDto.builder()
                    .accessToken("newAccessToken")
                    .refreshToken("newRefreshToken")
                    .build());
            when(jwtUtil.getTokenTypeFromToken(any(String.class))).thenReturn("REFRESH");

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/refresh"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Authorization이/가 비어있습니다."));
        }
    }
}
