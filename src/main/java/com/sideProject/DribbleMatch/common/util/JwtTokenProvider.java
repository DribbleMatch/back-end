package com.sideProject.DribbleMatch.common.util;

import com.sideProject.DribbleMatch.config.JwtConfig;
import com.sideProject.DribbleMatch.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final RedisUtil redisUtil;

    @Value("${spring.jwt.access-token-expire}")
    private Long ACCESS_TOKEN_EXPIRE_LENGTH;
    @Value("${spring.jwt.refresh-token-expire}")
    private Long REFRESH_TOKEN_EXPIRE_LENGTH;

    public String createAccessToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, String.valueOf(jwtConfig.SECRET_KEY))
                .claim("userId", user.getId())
                .claim("type", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        redisUtil.deleteByValue(user.getId().toString());

        String refreshToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, String.valueOf(jwtConfig.SECRET_KEY))
                .claim("userId", user.getId())
                .claim("type", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();

        redisUtil.setRefreshToken(refreshToken, user.getId());

        return refreshToken;
    }
}
