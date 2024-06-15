package com.sideProject.DribbleMatch.service.auth;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.JwtTokenProvider;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.common.util.RedisUtil;
import com.sideProject.DribbleMatch.dto.user.request.UserSignInRequest;
import com.sideProject.DribbleMatch.dto.user.response.JwtResonseDto;
import com.sideProject.DribbleMatch.entity.user.Admin;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.AdminRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
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
    public JwtResonseDto userSignIn(UserSignInRequest request) {
        User user = validateUser(request);
        return JwtResonseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user))
                .refreshToken(jwtTokenProvider.createRefreshToken(user))
                .build();
    }


    @Override
    public JwtResonseDto adminSignIn(UserSignInRequest request) {
        Admin admin = validateAdmin(request);
        return JwtResonseDto.builder()
                .accessToken(jwtTokenProvider.createAdminAccessToken(admin))
                .refreshToken(jwtTokenProvider.createAdminRefreshToken(admin))
                .build();
    }

    @Override
    public JwtResonseDto refresh(String refreshToken) {
        jwtUtil.validateRefreshToken(refreshToken);
        String userId = redisUtil.getData(refreshToken);

        // refresh token 유효 기간 지나면 validation에서 에러 발생하지만 double check
        if(userId.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        return JwtResonseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user))
                .refreshToken(jwtTokenProvider.createRefreshToken(user))
                .build();
    }

    @Override
    public JwtResonseDto adminRefresh(String refreshToken) {
        jwtUtil.validateRefreshToken(refreshToken);
        String adminId = redisUtil.getData(refreshToken);

        // refresh token 유효 기간 지나면 validation에서 에러 발생하지만 double check
        if(adminId.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Admin admin = adminRepository.findById(Long.valueOf(adminId.replace("A", ""))).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ADMIN_ID));
        return JwtResonseDto.builder()
                .accessToken(jwtTokenProvider.createAdminAccessToken(admin))
                .refreshToken(jwtTokenProvider.createAdminRefreshToken(admin))
                .build();
    }

    private User validateUser(UserSignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_EMAIL));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        return user;
    }

    private Admin validateAdmin(UserSignInRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_EMAIL));
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        return admin;
    }
}
