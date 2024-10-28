package com.sideProject.DribbleMatch.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 쿠키에서 Authorization 토큰 찾기
        Optional<String> optionalJwtToken = getTokenFromCookies(request);

        if (optionalJwtToken.isPresent()) {
            String jwtToken = optionalJwtToken.get();

            try {
                if (jwtUtil.getTokenTypeFromToken(jwtToken).equals("ACCESS")) {

                    jwtUtil.validateAccessToken(jwtToken);

                    Long userId = jwtUtil.getUserIdFromToken(jwtToken);
                    String role = jwtUtil.getMemberRoleFromToken(jwtToken);

                    Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                } else {
                    SecurityContextHolder.clearContext();
                    filterChain.doFilter(request, response);
                }
            } catch (JwtException e) {
                // 검증 실패
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
            }
        } else {
            // 토큰이 없는 경우
            filterChain.doFilter(request, response);
        }
    }

    private Optional<String> getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
}
