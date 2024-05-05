package com.sideProject.DribbleMatch.service.user;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.JwtTokenProvider;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.common.util.RedisUtil;
import com.sideProject.DribbleMatch.dto.user.response.JwtResonseDto;
import com.sideProject.DribbleMatch.dto.user.request.UserSignInRequest;
import com.sideProject.DribbleMatch.dto.user.request.UserSignUpRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.service.region.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegionService regionService;

    @Override
    @Transactional
    public Long signUp(UserSignUpRequestDto request) {

        // validation
        checkUniqueEmail(request.getEmail());
        validatePassword(request.getPassword());
        checkUniqueNickName(request.getNickName());

        Region region = regionService.findRegion(request.getRegionString());
        User signUpUser = userRepository.save(User.builder()
                        .email(request.getEmail())
                        .password(checkAndEncodePassword(request.getPassword(), request.getRePassword()))
                        .nickName(request.getNickName())
                        .gender(request.getGender())
                        .birth(request.getBirth())
                        .position(request.getPosition())
                        .winning(0)
                        .region(region)
                .build());

        return signUpUser.getId();
    }

    @Override
    public JwtResonseDto signIn(UserSignInRequest request) {
        User user = validateUser(request);
        return JwtResonseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user))
                .refreshToken(jwtTokenProvider.createRefreshToken(user))
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

    private void checkUniqueEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_EMAIL);
        }
    }

    private void checkUniqueNickName(String nickName) {
        if (userRepository.findByNickName(nickName).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_NICKNAME);
        }
    }

    private void validatePassword(String password) {
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") ||
                !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()-_=+\\\\|\\[{\\]};:'\",<.>/?].*")) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_PATTERN);
        }
    }

    private String checkAndEncodePassword(String password, String rePassword) {
        if (!password.equals(rePassword)) {
            throw new CustomException(ErrorCode.NOT_SAME_PASSWORD);
        }
        return passwordEncoder.encode(password);
    }

    private User validateUser(UserSignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_EMAIL));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        return user;
    }
}
