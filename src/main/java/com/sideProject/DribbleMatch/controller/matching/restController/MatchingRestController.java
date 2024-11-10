package com.sideProject.DribbleMatch.controller.matching.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingInputScoreRequestDto;
import com.sideProject.DribbleMatch.service.componentService.matchingJoin.MatchingJoinService;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingRestController {

    private final MatchingService matchingService;
    private final MatchingJoinService matchingJoinService;

    @PostMapping("/create")
    public ApiResponse<Long> createMatching(Principal principal,
                                            @RequestBody MatchingCreateRequestDto requestDto) {

        return ApiResponse.ok(matchingJoinService.createMatching(Long.valueOf(principal.getName()), requestDto));
    }

    @PostMapping("/inputScore")
    public ApiResponse<Boolean> inputScore(Principal principal,
                                           @RequestBody MatchingInputScoreRequestDto requestDto) {

        matchingService.inputScore(requestDto);

        return ApiResponse.ok(matchingService.checkHasNotInputScore(Long.valueOf(principal.getName())));
    }

    @GetMapping("/notFinishMatching/{matchingId}")
    public ApiResponse<Boolean> notFinishMatching(Principal principal,
                                                  @PathVariable Long matchingId) {

        matchingService.notPlayMatching(matchingId);

        return ApiResponse.ok(matchingService.checkHasNotInputScore(Long.valueOf(principal.getName())));
    }
}
