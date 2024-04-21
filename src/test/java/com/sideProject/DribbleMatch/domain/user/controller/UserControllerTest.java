package com.sideProject.DribbleMatch.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sideProject.DribbleMatch.common.error.GlobalExceptionHandler;
import com.sideProject.DribbleMatch.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.DribbleMatch.common.security.JwtTokenFilter;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.config.SecurityConfig;
import com.sideProject.DribbleMatch.domain.user.dto.JwtResonseDto;
import com.sideProject.DribbleMatch.domain.user.dto.UserSignInRequest;
import com.sideProject.DribbleMatch.domain.user.dto.UserSignUpRequestDto;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.service.UserService;
import com.sideProject.DribbleMatch.domain.user.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//todo: security 미동작
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {UserController.class, SecurityConfig.class})
@ActiveProfiles("test")
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @SpyBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("userSignUpTest")
    public class userSignUpTest {
        @DisplayName("회원가입 API를 호출한다")
        @Test
        public void userSignUp() throws Exception {

            // given
            String request = "{\"email\": \"test@test.com\", " +
                    "\"password\": \"test1234\", " +
                    "\"rePassword\" : \"test1234\", " +
                    "\"nickName\" : \"test\", " +
                    "\"gender\" : \"MALE\", " +
                    "\"birth\" : \"2001-01-01\", " + // Type에 맞지 않는 값
                    "\"position\" : \"CENTER\"}";

            // mocking
            when(userService.signUp(any(UserSignUpRequestDto.class))).thenReturn(1L);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signUp").with(csrf())
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
                    "\"rePassword\" : \"test1234\", " +
                    "\"nickName\" : \"test\", " +
                    "\"gender\" : \"MALE\", " +
                    "\"birth\" : \"2001-01-01\", " + // Type에 맞지 않는 값
                    "\"position\" : \"CENTER\"}";

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[비밀번호가 입력되지 않았습니다.]"))
            ;
        }

        @DisplayName("데이터 형식이나 타입이 잘못되면 에러가 발생한다")
        @Test
        public void userSignUp3() throws Exception {

            // given
            String request = "{\"email\": \"test@test.com\", " +
                    "\"password\": \"test1234\", " +
                    "\"rePassword\" : \"test1234\", " +
                    "\"nickName\" : \"test\", " +
                    "\"gender\" : \"MALE\", " +
                    "\"birth\" : \"test\", " + // Type에 맞지 않는 값
                    "\"position\" : \"CENTER\"}";

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
    @DisplayName("userSignInTest")
    public class userSignInTest {

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
            when(userService.signIn(any(UserSignInRequest.class))).thenReturn(response);

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
    @DisplayName("refreshTest")
    public class refreshTest {

        @DisplayName("Header에 있는 Refresh Token으로 토큰 재발급 API를 요청한다")
        @Test
        public void refresh() throws Exception {
            // given
            String refreshToken = "Bearer refreshToken";

            // mocking
            when(userService.refresh("refreshToken")).thenReturn(JwtResonseDto.builder()
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
            when(userService.refresh("refreshToken")).thenReturn(JwtResonseDto.builder()
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
