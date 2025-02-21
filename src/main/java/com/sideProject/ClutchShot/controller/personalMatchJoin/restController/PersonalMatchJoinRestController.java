package com.sideProject.ClutchShot.controller.personalMatchJoin.restController;

import com.sideProject.ClutchShot.common.response.ApiResponse;
import com.sideProject.ClutchShot.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.ClutchShot.service.componentService.matchingJoin.MatchingJoinService;
import com.sideProject.ClutchShot.service.personalMatchJoin.PersonalMatchJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/personalMatchJoin")
@RequiredArgsConstructor
public class PersonalMatchJoinRestController {

    private final PersonalMatchJoinService personalMatchJoinService;
    private final MatchingJoinService matchingJoinService;

    @PostMapping
    public ApiResponse<Long> joinPersonalMatch(Principal principal,
                                               @RequestParam(name = "matchingId") Long matchingId,
                                               @RequestParam(name = "team") PersonalMatchingTeam personalMatchingTeam) {

        return ApiResponse.ok(matchingJoinService.joinPersonalMatching(matchingId, Long.valueOf(principal.getName()), personalMatchingTeam));
    }
}
