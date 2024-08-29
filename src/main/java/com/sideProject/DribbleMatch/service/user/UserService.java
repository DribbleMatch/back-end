package com.sideProject.DribbleMatch.service.user;

import com.sideProject.DribbleMatch.dto.user.request.SignupPlayerInfoRequestDto;

public interface UserService {
    public void checkNickName(String nickName);
    public void checkEmail(String email);
    public void sendAuthMessage(String phone);
    public void getAuth(String phone, String authCode);
    public void signUp(SignupPlayerInfoRequestDto requestDto);
}
