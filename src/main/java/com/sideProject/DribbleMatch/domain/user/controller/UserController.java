package com.sideProject.DribbleMatch.domain.user.controller;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.domain.user.dto.JwtResonseDto;
import com.sideProject.DribbleMatch.domain.user.dto.UserSignInRequest;
import com.sideProject.DribbleMatch.domain.user.dto.UserSignUpRequestDto;
import com.sideProject.DribbleMatch.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String ok() {
        throw new CustomException(ErrorCode.TEST_ERROR);
    }

    // 회원가입
    @PostMapping("/signUp")
    public ApiResponse<Long> signUp(@Valid @RequestBody UserSignUpRequestDto request) {
        return ApiResponse.ok(userService.signUp(request));
    }

    // 로그인
    @PostMapping("/signIn")
    public ApiResponse<JwtResonseDto> signIn(@Valid @RequestBody UserSignInRequest request) {
        return ApiResponse.ok(userService.signIn(request));
    }

    // 토큰 재발급
    @GetMapping("/refresh")
    public ApiResponse<JwtResonseDto> refresh(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(7);
        return ApiResponse.ok(userService.refresh(refreshToken));
    }
}
