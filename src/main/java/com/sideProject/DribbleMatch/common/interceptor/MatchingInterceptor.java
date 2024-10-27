package com.sideProject.DribbleMatch.common.interceptor;

import com.sideProject.DribbleMatch.service.matching.MatchingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MatchingInterceptor implements HandlerInterceptor {

    private final MatchingService matchingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getUserPrincipal().getName();

        if (!matchingService.checkHasNotInputScore(Long.valueOf(userId))) {
            response.sendRedirect("/page/matching/inputScore");
            return false;
        }

        return true;
    }
}