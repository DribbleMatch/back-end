package com.sideProject.ClutchShot.service.auth;

import com.sideProject.ClutchShot.common.error.CustomException;
import com.sideProject.ClutchShot.common.error.ErrorCode;
import com.sideProject.ClutchShot.common.util.JwtTokenProvider;
import com.sideProject.ClutchShot.common.util.JwtUtil;
import com.sideProject.ClutchShot.common.util.RedisUtil;
import com.sideProject.ClutchShot.dto.user.response.JwtResponseDto;
import com.sideProject.ClutchShot.entity.user.User;
import com.sideProject.ClutchShot.repository.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    @Value("${spring.jwt.access-token-expire}")
    private Long ACCESS_TOKEN_EXPIRE_LENGTH;
    @Value("${spring.jwt.refresh-token-expire}")
    private Long REFRESH_TOKEN_EXPIRE_LENGTH;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public void setCookie(JwtResponseDto tokens, HttpServletResponse response) {

        Cookie accessToken = new Cookie("accessToken", tokens.getAccessToken());
        accessToken.setMaxAge((int) (ACCESS_TOKEN_EXPIRE_LENGTH / 1000));
        accessToken.setPath("/");
        accessToken.setHttpOnly(true);

        Cookie refreshToken = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshToken.setMaxAge((int) (REFRESH_TOKEN_EXPIRE_LENGTH / 100));
        refreshToken.setPath("/");
        refreshToken.setHttpOnly(true);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }

    @Override
    public void deleteCookie(HttpServletResponse response){
        Cookie accessToken = new Cookie("accessToken", null);
        accessToken.setMaxAge(0);
        accessToken.setPath("/");
        response.addCookie(accessToken);

        Cookie refreshToken = new Cookie("refreshToken", null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");
        response.addCookie(refreshToken);
    }
}
