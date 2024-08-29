package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.config.SecurityConfig;
import com.sideProject.DribbleMatch.controller.user.restController.AdminController;
import com.sideProject.DribbleMatch.service.user.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {AdminController.class, SecurityConfig.class})
@ActiveProfiles("test")
public class AuthControllerTest {

    @SpyBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    private final Long userId = 1L;

    @Nested
    @DisplayName("AuthoritiesControllerTest")
    public class AuthoritiesControllerTest {

        @DisplayName("관리자만 요청할 수 있는 URL에 관리자가 요청을 보낸다")
        @Test
        @WithMockUser(username = "1", authorities = {"ADMIN"})
        public void authTest() throws Exception {

            // given
            Long userToChangeId = 1L;
            Long adminId = 2L;

            // mocking
            when(adminService.changeToAdmin(userToChangeId)).thenReturn(adminId);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.put("/api/admin/{userId}" ,userToChangeId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(adminId));
        }

        @DisplayName("관리자만 요청할 수 있는 URL에 요청을 보낸 사람이 관리자가 아니면 에러가 발생한다")
        @Test
        @WithMockUser(username = "1")
        public void authTest2() throws Exception {

            // given
            Long userToChangeId = 1L;
            Long adminId = 2L;

            // mocking
            when(adminService.changeToAdmin(userToChangeId)).thenReturn(adminId);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.put("/api/admin/" + userToChangeId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

    }
}
