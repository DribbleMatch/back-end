package com.sideProject.DribbleMatch.service.user;

import com.sideProject.DribbleMatch.dto.user.response.JwtResonseDto;
import com.sideProject.DribbleMatch.dto.user.request.UserSignInRequest;
import com.sideProject.DribbleMatch.dto.user.request.UserSignUpRequestDto;

public interface UserService {
    public Long signUp(UserSignUpRequestDto request);
    public JwtResonseDto signIn(UserSignInRequest request);
    public JwtResonseDto refresh(String refreshToken);
}
