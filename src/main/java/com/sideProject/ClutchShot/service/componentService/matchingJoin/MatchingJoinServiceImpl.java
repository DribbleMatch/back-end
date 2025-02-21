package com.sideProject.ClutchShot.service.componentService.matchingJoin;

import com.sideProject.ClutchShot.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.ClutchShot.entity.matching.ENUM.GameKind;
import com.sideProject.ClutchShot.entity.matching.Matching;
import com.sideProject.ClutchShot.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.ClutchShot.service.matching.MatchingService;
import com.sideProject.ClutchShot.service.personalMatchJoin.PersonalMatchJoinService;
import com.sideProject.ClutchShot.service.teamMatchJoin.TeamMatchJoinService;
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

    @Override
    @Transactional
    public Long joinPersonalMatching(Long matchingId, Long userId, PersonalMatchingTeam personalMatchingTeam) {

        personalMatchJoinService.updateMatchingStatus(matchingId);

        return personalMatchJoinService.createPersonalMatchJoin(matchingId, userId, personalMatchingTeam);
    }
}
