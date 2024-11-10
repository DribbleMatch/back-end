package com.sideProject.DribbleMatch.controller.teamMatchJoin.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.service.componentService.matchingJoin.MatchingJoinService;
import com.sideProject.DribbleMatch.service.teamMatchJoin.TeamMatchJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/teamMatchJoin")
@RequiredArgsConstructor
public class TeamMatchJoinRestController {

    private final TeamMatchJoinService teamMatchJoinService;
    private final MatchingJoinService matchingJoinService;

    @PostMapping
    public ApiResponse<Long> joinTeamMatch(Principal principal,
                              @RequestParam(name = "matchingId") Long matchingId,
                              @RequestParam(name = "teamName") String teamName) {

        return ApiResponse.ok(matchingJoinService.joinTeamMatching(matchingId, Long.valueOf(principal.getName()), teamName));
    }
}
