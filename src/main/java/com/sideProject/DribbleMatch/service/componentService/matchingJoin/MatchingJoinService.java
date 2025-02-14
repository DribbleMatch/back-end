package com.sideProject.DribbleMatch.service.componentService.matchingJoin;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;

public interface MatchingJoinService {

    public Long createMatching(Long userId, MatchingCreateRequestDto requestDto);
    public Long joinTeamMatching(Long matchingId, Long userId, String teamName);
    public Long joinPersonalMatching(Long matchingId, Long userId, PersonalMatchingTeam personalMatchingTeam);
}
