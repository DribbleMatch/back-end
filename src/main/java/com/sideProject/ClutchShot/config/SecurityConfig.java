package com.sideProject.ClutchShot.config;

import com.sideProject.ClutchShot.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.ClutchShot.common.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                })
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(
                                "/page/signup/**", // 회원가입 페이지
                                "/api/signup/**", // 회원가입 REST API
                                "/page/login/**", // 로그인 페이지
                                "/api/login/**", // 회원가입 REST API
                                "/api/region/**",
                                "/api/image**",
                                "/page",
                                "/error",
                                "/healthcheck",
                                "/css/**",
                                "/js/**",
                                "/image/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
