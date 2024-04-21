package com.sideProject.DribbleMatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${spring.jwt.secret-key}")
    public String SECRET_KEY;
}
