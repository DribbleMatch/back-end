package com.sideProject.DribbleMatch.controller.user.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.service.user.UserService;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup/rest")
public class SignUpRestController {

    private final UserService userService;

    // 닉네임 중복 확인
    @PostMapping("/checkNickName")
    public ApiResponse<String> checkNickName(@RequestHeader Map<String, String> headers, @RequestParam("nickName") String nickName) {
        userService.checkNickName(nickName);
        return ApiResponse.ok("사용 가능한 닉네임입니다.");
    }

    // 이메일 중복 확인
    @PostMapping("/checkEmail")
    public ApiResponse<String> checkEmail(@RequestHeader Map<String, String> headers, @RequestParam("email") String email) {
        userService.checkEmail(email);
        return ApiResponse.ok("사용 가능한 이메일입니다.");
    }

    // 휴대폰 번호 인증 메세지 전송
    @PostMapping("/sendAuthMessage")
    public ApiResponse<String> sendAuthMessage(@RequestParam("phone") String phone) {
        userService.sendAuthMessage(phone);
        return ApiResponse.ok("인증번호가 전송되었습니다.");
    }

    // 휴대폰 번호 인증 번호 확인
    @PostMapping("/getAuth")
    public ApiResponse<String> getAuth(@RequestParam("phone") String phone, @RequestParam String authCode) {
        userService.getAuth(phone, authCode);
        return ApiResponse.ok("인증이 완료되었습니다.");
    }
}
