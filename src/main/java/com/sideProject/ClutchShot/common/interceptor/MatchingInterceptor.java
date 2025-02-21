package com.sideProject.ClutchShot.common.interceptor;

import com.sideProject.ClutchShot.service.matching.MatchingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MatchingInterceptor implements HandlerInterceptor {

    private final MatchingService matchingService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

        String userId = request.getUserPrincipal().getName();

        if (!matchingService.checkHasNotInputScore(Long.valueOf(userId))) {
            request.setAttribute("haveInput", 1);
            // 점수 입력 필요
        } else {
            request.setAttribute("haveInput", 0);
        }

        return true;
    }
}