package com.sideProject.ClutchShot.service.user;

import com.querydsl.core.Tuple;
import com.sideProject.ClutchShot.common.error.CustomException;
import com.sideProject.ClutchShot.common.error.ErrorCode;
import com.sideProject.ClutchShot.common.util.*;
import com.sideProject.ClutchShot.dto.user.request.ChangePasswordRequestDto;
import com.sideProject.ClutchShot.dto.user.request.FindInfoRequestDto;
import com.sideProject.ClutchShot.dto.user.request.SignupPlayerInfoRequestDto;
import com.sideProject.ClutchShot.dto.user.request.UserLogInRequestDto;
import com.sideProject.ClutchShot.dto.user.response.JwtResponseDto;
import com.sideProject.ClutchShot.dto.user.response.UserResponseDto;
import com.sideProject.ClutchShot.entity.region.Region;
import com.sideProject.ClutchShot.entity.user.ENUM.Gender;
import com.sideProject.ClutchShot.entity.user.User;
import com.sideProject.ClutchShot.repository.region.RegionRepository;
import com.sideProject.ClutchShot.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void checkEmailAndPhone(String email, String phone) {
        if (userRepository.findByEmailAndPhone(email, phone).isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_EMAIL);
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


    @Override
    @Transactional
    public void createUser(SignupPlayerInfoRequestDto requestDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birth = LocalDate.parse(requestDto.getBirth(), formatter);

        String password = passwordEncoder.encode(requestDto.getPassword());
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
                .imagePath(requestDto.getImage().isEmpty() ? path + File.separator + "user_default_image.png" : fileUtil.saveImage(requestDto.getImage(), path, requestDto.getNickName()))
                .phone(requestDto.getPhone())
                .career(requestDto.getCareer())
                .skill(requestDto.getSkill())
                .experience(0)
                .build();

        userRepository.save(signupUser);
    }

    @Override
    public UserResponseDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));
        String regionString = regionRepository.findRegionStringById(user.getRegion().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));

        return UserResponseDto.builder()
                .imagePath(user.getImagePath())
                .nickName(user.getNickName())
                .positionString(CommonUtil.createPositionString(user.getPositionString()))
                .ageAndGender(CommonUtil.calculateAge(user.getBirth()) + "세 / " + (user.getGender() == Gender.MALE ? "남성" : "여성"))
                .skillString(CommonUtil.createSkillString(user.getSkill()))
                .regionString(regionString)
                .level(CommonUtil.getLevel(user.getExperience()))
                .experience(CommonUtil.getExperiencePercentToLevelUp(user.getExperience()))
                .build();
    }

    @Override
    public String getUserNickName(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER)).getNickName();
    }

    @Override
    public List<Map<String, Object>> getEmailList(FindInfoRequestDto requestDto) {

        List<Tuple> emailInfoList =  userRepository.findAllEmailByUserInfo(requestDto.getName(), requestDto.getPhoneNum());

        return emailInfoList.stream()
                .map(tuple -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", tuple.get(0, String.class));
                    map.put("createdAt", tuple.get(1, LocalDate.class));
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Long getUserId(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_EMAIL)).getId();
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));

        if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.SAME_PASSWORD_RESET);
        }

        user.changePassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
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
