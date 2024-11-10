package com.sideProject.DribbleMatch.service.teamMatchJoin;

import com.sideProject.DribbleMatch.entity.matching.Matching;

public interface TeamMatchJoinService {
    public Matching createTeamMatchJoin(Long matchingId, Long userId, String teamName);

    public void checkAlreadyJoin(Long matchingId, Long userId);
}
