package com.sideProject.DribbleMatch.service.componentService.matchingJoin;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;

public interface MatchingJoinService {

    public Long createMatching(Long userId, MatchingCreateRequestDto requestDto);
    public Long joinTeamMatching(Long matchingId, Long userId, String teamName);
}
