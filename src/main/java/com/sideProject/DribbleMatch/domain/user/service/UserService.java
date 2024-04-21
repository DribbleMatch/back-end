package com.sideProject.DribbleMatch.domain.user.service;

import com.sideProject.DribbleMatch.domain.user.dto.JwtResonseDto;
import com.sideProject.DribbleMatch.domain.user.dto.UserSignInRequest;
import com.sideProject.DribbleMatch.domain.user.dto.UserSignUpRequestDto;

public interface UserService {
    public Long signUp(UserSignUpRequestDto request);
    public JwtResonseDto signIn(UserSignInRequest request);
    public JwtResonseDto refresh(String refreshToken);
}
