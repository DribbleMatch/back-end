package com.sideProject.ClutchShot.controller.user.restController;

import com.sideProject.ClutchShot.common.response.ApiResponse;
import com.sideProject.ClutchShot.dto.user.request.ChangePasswordRequestDto;
import com.sideProject.ClutchShot.dto.user.request.UserLogInRequestDto;
import com.sideProject.ClutchShot.dto.user.response.JwtResponseDto;
import com.sideProject.ClutchShot.service.auth.AuthService;
import com.sideProject.ClutchShot.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginRestController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    public ApiResponse<String> userLogin(@RequestBody UserLogInRequestDto requestDto,
                                         HttpServletResponse response) {

        JwtResponseDto tokens = userService.login(requestDto);

        authService.setCookie(tokens, response);

        return ApiResponse.ok("로그인 성공");
    }

    @GetMapping("/logout")
    public ApiResponse<String> userLogout(HttpServletResponse response) {

        authService.deleteCookie(response);

        return ApiResponse.ok("로그아웃 성공");
    }

    @GetMapping("/checkLogIn")
    public ApiResponse<String> checkLogIn(Principal principal) {

        if (principal != null) {
            return ApiResponse.ok(userService.getUserNickName(Long.valueOf(principal.getName())));
        }

        return ApiResponse.ok("");
    }

    @PostMapping("/changePassword")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequestDto requestDto) {
        userService.changePassword(requestDto);
        return ApiResponse.ok("비밀번호 변경 완료");
    }
}
