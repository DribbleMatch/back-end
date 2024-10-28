package com.sideProject.DribbleMatch.service.user;

import com.sideProject.DribbleMatch.dto.user.request.SignupPlayerInfoRequestDto;
import com.sideProject.DribbleMatch.dto.user.request.UserLogInRequestDto;
import com.sideProject.DribbleMatch.dto.user.response.JwtResponseDto;
import com.sideProject.DribbleMatch.dto.user.response.UserResponseDto;

public interface UserService {
    public void checkNickName(String nickName);
    public void checkEmail(String email);
    public void sendAuthMessage(String phone);
    public void getAuth(String phone, String authCode);
    public JwtResponseDto login(UserLogInRequestDto requestDto);
    public void createUser(SignupPlayerInfoRequestDto requestDto);
    public UserResponseDto getUserDetail(Long userId);
    public String getUserNickName(Long userId);
}
