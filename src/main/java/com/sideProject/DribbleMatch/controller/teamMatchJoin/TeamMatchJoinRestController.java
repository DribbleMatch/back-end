package com.sideProject.DribbleMatch.controller.teamMatchJoin;

import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.service.teamMatchJoin.TeamMatchJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/teamMatchJoin/rest")
@RequiredArgsConstructor
public class TeamMatchJoinRestController {

    private final TeamMatchJoinService teamMatchJoinService;

    @PostMapping
    public void joinTeamMatch(Principal principal,
                              @RequestParam(name = "matchingId") Long matchingId,
                              @RequestParam(name = "teamName") String teamName) {
        teamMatchJoinService.createTeamMatchJoin(matchingId, Long.valueOf(principal.getName()), teamName);
    }
}
