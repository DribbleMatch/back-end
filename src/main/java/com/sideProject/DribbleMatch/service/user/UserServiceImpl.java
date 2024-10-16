package com.sideProject.DribbleMatch.service.user;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.*;
import com.sideProject.DribbleMatch.dto.user.request.SignupPlayerInfoRequestDto;
import com.sideProject.DribbleMatch.dto.user.request.UserLogInRequestDto;
import com.sideProject.DribbleMatch.dto.user.response.JwtResponseDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    @Value("${spring.dir.userImagePath}")
    public String path;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;
    private final RedisUtil redisUtil;
    private final SmsUtil smsUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileUtil fileUtil;

    @Override
    public void checkNickName(String nickName) {
        if(userRepository.findByNickName(nickName).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_NICKNAME);
        }
    }

    @Override
    public void checkEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_EMAIL);
        }
    }

    @Override
    public void sendAuthMessage(String phone) {

        String authCode = generateAuthCode();

        try {
            smsUtil.sendOne(phone, authCode);
            redisUtil.setAuthCode(phone, authCode);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_SEND_AUTH_MESSAGE);
        }
    }

    @Override
    public void getAuth(String phone, String authCode) {

        if(!redisUtil.getData(phone).equals(authCode)) {
            throw new CustomException(ErrorCode.NOT_CORRECT_AUTH_CODE);
        }
        redisUtil.deleteData(phone);
        redisUtil.setAuthCode(phone, "success");
    }

    @Override
    @Transactional
    public void createUser(SignupPlayerInfoRequestDto requestDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birth = LocalDate.parse(requestDto.getBirth(), formatter);

        String password = encodePassword(requestDto.getPassword());
        String regionString = requestDto.getSiDoString() + " " + requestDto.getSiGunGuString();
        Region region = regionRepository.findByRegionString(regionString).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));

        User signupUser = User.builder()
                .email(requestDto.getEmail())
                .password(password)
                .nickName(requestDto.getNickName())
                .gender(requestDto.getGender())
                .birth(birth)
                .positionString(requestDto.getPositionString())
                .winning(0)
                .region(region)
                .imagePath(fileUtil.saveImage(requestDto.getImage(), path, requestDto.getNickName()))
                .build();

        userRepository.save(signupUser);
    }

    @Override
    public JwtResponseDto login(UserLogInRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_EMAIL));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        } else {
            return JwtResponseDto.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(user))
                    .refreshToken(jwtTokenProvider.createRefreshToken(user))
                    .build();
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateAuthCode() {

        SecureRandom random = new SecureRandom();
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder authCode = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            authCode.append(CHARACTERS.charAt(index));
        }
        return authCode.toString();
    }
}
