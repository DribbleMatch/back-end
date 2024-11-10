package com.sideProject.DribbleMatch.service.componentService.matchingJoin;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import com.sideProject.DribbleMatch.service.personalMatchJoin.PersonalMatchJoinService;
import com.sideProject.DribbleMatch.service.teamMatchJoin.TeamMatchJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingJoinServiceImpl implements MatchingJoinService{

    private final MatchingService matchingService;
    private final TeamMatchJoinService teamMatchJoinService;
    private final PersonalMatchJoinService personalMatchJoinService;

    @Override
    @Transactional
    public Long createMatching(Long userId, MatchingCreateRequestDto requestDto) {

        Long matchingId = matchingService.createMatching(requestDto, userId);

        if (requestDto.getGameKind() == GameKind.TEAM) {
            teamMatchJoinService.createTeamMatchJoin(matchingId, userId, requestDto.getTeamName());
        } else if (requestDto.getGameKind() == GameKind.PERSONAL) {
            personalMatchJoinService.createPersonalMatchJoin(matchingId, userId, PersonalMatchingTeam.UP_TEAM);
        }

        return matchingId;
    }

    @Override
    @Transactional
    public Long joinTeamMatching(Long matchingId, Long userId, String teamName) {

        Matching matching = teamMatchJoinService.createTeamMatchJoin(matchingId, userId, teamName);
        return matchingService.changeDownTeamName(matching, teamName);
    }
}
