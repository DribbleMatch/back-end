package com.sideProject.DribbleMatch.service.auth;

import com.sideProject.DribbleMatch.dto.user.request.UserSignInRequest;
import com.sideProject.DribbleMatch.dto.user.response.JwtResonseDto;

public interface AuthService {
    public JwtResonseDto userSignIn(UserSignInRequest request);
    public JwtResonseDto adminSignIn(UserSignInRequest request);
    public JwtResonseDto refresh(String refreshToken);
    public JwtResonseDto adminRefresh(String refreshToken);
}
