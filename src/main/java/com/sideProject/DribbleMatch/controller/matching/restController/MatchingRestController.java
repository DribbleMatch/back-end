package com.sideProject.DribbleMatch.controller.matching.restController;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import com.sideProject.DribbleMatch.repository.personalMatchJoin.PersonalMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import com.sideProject.DribbleMatch.service.personalMatchJoin.PersonalMatchJoinService;
import com.sideProject.DribbleMatch.service.teamMatchJoin.TeamMatchJoinService;
import com.sideProject.DribbleMatch.service.teamMember.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingRestController {

    private final MatchingService matchingService;
    private final TeamMatchJoinService teamMatchJoinService;
    private final PersonalMatchJoinService personalMatchJoinService;

    //todo: 트랜잭션 처리 정리하기
    @PostMapping("/createMatching")
    public Long createMatching(Principal principal, @RequestBody MatchingCreateRequestDto requestDto) {

        Long matchingId = matchingService.createMatching(requestDto);

        if (requestDto.getGameKind() == GameKind.TEAM) {
            teamMatchJoinService.createTeamMatchJoin(matchingId, Long.valueOf(principal.getName()), requestDto.getTeamName());
        } else if (requestDto.getGameKind() == GameKind.PERSONAL) {
            personalMatchJoinService.createPersonalMatchJoin(matchingId, Long.valueOf(principal.getName()), PersonalMatchingTeam.UP_TEAM);
        }

        return matchingId;
    }

    @GetMapping("/asdf")
    public void testtesttest() {

        throw new CustomException(ErrorCode.TEST_ERROR);
    }
}
