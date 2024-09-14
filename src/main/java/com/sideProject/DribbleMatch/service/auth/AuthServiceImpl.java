package com.sideProject.DribbleMatch.service.auth;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.JwtTokenProvider;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.common.util.RedisUtil;
import com.sideProject.DribbleMatch.dto.user.request.UserLogInRequestDto;
import com.sideProject.DribbleMatch.dto.user.response.JwtResponseDto;
import com.sideProject.DribbleMatch.entity.user.Admin;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.AdminRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDto userSignIn(UserLogInRequestDto request) {
        User user = validateUser(request);
        return JwtResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user))
                .refreshToken(jwtTokenProvider.createRefreshToken(user))
                .build();
    }


    @Override
    public JwtResponseDto adminSignIn(UserLogInRequestDto request) {
        Admin admin = validateAdmin(request);
        return JwtResponseDto.builder()
                .accessToken(jwtTokenProvider.createAdminAccessToken(admin))
                .refreshToken(jwtTokenProvider.createAdminRefreshToken(admin))
                .build();
    }

    @Override
    public JwtResponseDto refresh(String refreshToken) {
        jwtUtil.validateRefreshToken(refreshToken);
        String userId = redisUtil.getData(refreshToken);

        // refresh token 유효 기간 지나면 validation에서 에러 발생하지만 double check
        if(userId.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));
        return JwtResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user))
                .refreshToken(jwtTokenProvider.createRefreshToken(user))
                .build();
    }

    @Override
    public JwtResponseDto adminRefresh(String refreshToken) {
        jwtUtil.validateRefreshToken(refreshToken);
        String adminId = redisUtil.getData(refreshToken);

        // refresh token 유효 기간 지나면 validation에서 에러 발생하지만 double check
        if(adminId.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Admin admin = adminRepository.findById(Long.valueOf(adminId.replace("A", ""))).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ADMIN));
        return JwtResponseDto.builder()
                .accessToken(jwtTokenProvider.createAdminAccessToken(admin))
                .refreshToken(jwtTokenProvider.createAdminRefreshToken(admin))
                .build();
    }

    @Override
    public void setCookie(JwtResponseDto tokens, HttpServletResponse response) {

        Cookie accessToken = new Cookie("accessToken", tokens.getAccessToken());
        accessToken.setMaxAge(1000000 / 1000);
        accessToken.setPath("/");
        accessToken.setHttpOnly(true);

        Cookie refreshToken = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshToken.setMaxAge(1000000 / 1000);
        refreshToken.setPath("/");
        refreshToken.setHttpOnly(true);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }

    @Override
    public void deleteCookie(HttpServletResponse response){
        Cookie accessToken = new Cookie("accessToken", null);
        accessToken.setMaxAge(0);  // 쿠키 즉시 삭제
        accessToken.setPath("/");  // 쿠키의 경로를 설정 (생성할 때 설정된 경로와 동일해야 삭제 가능)
        response.addCookie(accessToken);

        Cookie refreshToken = new Cookie("refreshToken", null);
        refreshToken.setMaxAge(0);  // 쿠키 즉시 삭제
        refreshToken.setPath("/");  // 쿠키의 경로를 설정 (생성할 때 설정된 경로와 동일해야 삭제 가능)
        response.addCookie(refreshToken);
    }

    private User validateUser(UserLogInRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_EMAIL));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        return user;
    }

    private Admin validateAdmin(UserLogInRequestDto request) {
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_EMAIL));
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        return admin;
    }
}
