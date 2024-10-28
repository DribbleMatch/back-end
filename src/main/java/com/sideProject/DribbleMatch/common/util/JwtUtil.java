package com.sideProject.DribbleMatch.common.util;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private final RedisUtil redisUtil;

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token).getBody();
        return Long.valueOf(claims.get("userId").toString());
    }

    public String getTokenTypeFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("type").toString();
    }

    public String getMemberRoleFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("role").toString();
    }

    public void validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public void validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            redisUtil.deleteData(token);
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
