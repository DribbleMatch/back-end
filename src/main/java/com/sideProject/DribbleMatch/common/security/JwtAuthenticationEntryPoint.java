package com.sideProject.DribbleMatch.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.user.response.JwtResponseDto;
import com.sideProject.DribbleMatch.service.auth.AuthService;
import com.sideProject.DribbleMatch.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String refreshToken = null;

        // refresh 토큰 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            authFailHandler(request, response);
        } else {
            try {
                JwtResponseDto tokens = authService.refresh(refreshToken);
                authService.setCookie(tokens, response);

                if (request.getRequestURI().contains("page")) {
                    response.sendRedirect(request.getRequestURI());
                } else {
                    String jsonResponse = objectMapper.writeValueAsString(ApiResponse.error(ErrorCode.TOKEN_REGENERATE));

                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(jsonResponse);
                }
            } catch (Exception exception) {
                authService.deleteCookie(response);
                authFailHandler(request, response);
            }
        }
    }

    //todo: replaceWith에는 로그인 페이지로 리다이렉트 안되고 replace되는 현상 해결
    private void authFailHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().contains("page")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendRedirect("/login/page");
        } else {
            String jsonResponse = objectMapper.writeValueAsString(ApiResponse.error(ErrorCode.INVALID_REFRESH_TOKEN));

            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(jsonResponse);
        }
    }
}
