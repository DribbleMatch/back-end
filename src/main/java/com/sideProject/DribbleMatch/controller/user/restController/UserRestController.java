package com.sideProject.DribbleMatch.controller.user.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.user.response.JwtResponseDto;
import com.sideProject.DribbleMatch.dto.user.request.UserLogInRequestDto;
import com.sideProject.DribbleMatch.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserRestController {

    private final AuthService authService;

    // 로그인
    @PostMapping("/signIn")
    public ApiResponse<JwtResponseDto> signIn(@Valid @RequestBody UserLogInRequestDto request) {
        return ApiResponse.ok(authService.userSignIn(request));
    }

    // 토큰 재발급
    @GetMapping("/refresh")
    public ApiResponse<JwtResponseDto> refresh(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(7);
        return ApiResponse.ok(authService.refresh(refreshToken));
    }
}
