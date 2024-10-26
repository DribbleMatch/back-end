package com.sideProject.DribbleMatch.controller.user.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.user.request.UserLogInRequestDto;
import com.sideProject.DribbleMatch.dto.user.response.JwtResponseDto;
import com.sideProject.DribbleMatch.service.auth.AuthService;
import com.sideProject.DribbleMatch.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginRestController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    public ApiResponse<String> userLogin(@RequestBody UserLogInRequestDto requestDto, HttpServletResponse response) {

        JwtResponseDto tokens = userService.login(requestDto);

        authService.setCookie(tokens, response);

        return ApiResponse.ok("로그인 성공");
    }

    @GetMapping("/logout")
    public ApiResponse<String> userLogout(HttpServletResponse response) {

        authService.deleteCookie(response);

        return ApiResponse.ok("로그아웃 성공");
    }
}
