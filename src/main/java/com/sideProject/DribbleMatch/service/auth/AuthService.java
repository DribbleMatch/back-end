package com.sideProject.DribbleMatch.service.auth;

import com.sideProject.DribbleMatch.dto.user.request.UserLogInRequestDto;
import com.sideProject.DribbleMatch.dto.user.response.JwtResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public JwtResponseDto userSignIn(UserLogInRequestDto request);
    public JwtResponseDto adminSignIn(UserLogInRequestDto request);
    public JwtResponseDto refresh(String refreshToken);
    public JwtResponseDto adminRefresh(String refreshToken);
    public void setCookie(JwtResponseDto tokens, HttpServletResponse response);
    public void deleteCookie(HttpServletResponse response);
}
