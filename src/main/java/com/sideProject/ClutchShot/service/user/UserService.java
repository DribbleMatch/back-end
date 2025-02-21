package com.sideProject.ClutchShot.service.user;

import com.sideProject.ClutchShot.dto.user.request.ChangePasswordRequestDto;
import com.sideProject.ClutchShot.dto.user.request.FindInfoRequestDto;
import com.sideProject.ClutchShot.dto.user.request.SignupPlayerInfoRequestDto;
import com.sideProject.ClutchShot.dto.user.request.UserLogInRequestDto;
import com.sideProject.ClutchShot.dto.user.response.JwtResponseDto;
import com.sideProject.ClutchShot.dto.user.response.UserResponseDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    public void checkNickName(String nickName);
    public void checkEmail(String email);
    public void checkEmailAndPhone(String email, String phone);
    public void sendAuthMessage(String phone);
    public void getAuth(String phone, String authCode);
    public JwtResponseDto login(UserLogInRequestDto requestDto);
    public void createUser(SignupPlayerInfoRequestDto requestDto);
    public UserResponseDto getUserDetail(Long userId);
    public String getUserNickName(Long userId);
    public List<Map<String, Object>> getEmailList(FindInfoRequestDto requestDto);
    public Long getUserId(String email);
    public void changePassword(ChangePasswordRequestDto requestDto);
}
