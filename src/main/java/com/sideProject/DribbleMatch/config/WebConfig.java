package com.sideProject.DribbleMatch.config;

import com.sideProject.DribbleMatch.common.interceptor.MatchingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MatchingInterceptor matchingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(matchingInterceptor)
                .addPathPatterns("/page/matching/**", "/api/matching/**")
                .excludePathPatterns("/page/matching/inputScore")
                .excludePathPatterns("/api/matching/inputScore")
                .excludePathPatterns("/api/matching/notFinishMatching/**");
    }
}