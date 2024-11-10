package com.sideProject.DribbleMatch.controller.personalMatchJoin.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.service.personalMatchJoin.PersonalMatchJoinService;
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

    @PostMapping
    public ApiResponse<Long> joinPersonalMatch(Principal principal,
                                         @RequestParam(name = "matchingId") Long matchingId,
                                         @RequestParam(name = "team") PersonalMatchingTeam personalMatchingTeam) {

        return ApiResponse.ok(personalMatchJoinService.createPersonalMatchJoin(matchingId, Long.valueOf(principal.getName()), personalMatchingTeam));
    }
}
