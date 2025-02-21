package com.sideProject.ClutchShot.service.teamMatchJoin;

import com.sideProject.ClutchShot.entity.matching.Matching;

public interface TeamMatchJoinService {
    public Matching createTeamMatchJoin(Long matchingId, Long userId, String teamName);

    public void checkAlreadyJoin(Long matchingId, Long userId);
    public void checkMaxNum(Matching matching, int teamMemberNum);
}
