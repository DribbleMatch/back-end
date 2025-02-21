package com.sideProject.ClutchShot.service.personalMatchJoin;

import com.sideProject.ClutchShot.entity.matching.Matching;
import com.sideProject.ClutchShot.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;

public interface PersonalMatchJoinService {

    public Long createPersonalMatchJoin(Long matchingId, Long userId, PersonalMatchingTeam personalMatchingTeam);
    public void updateMatchingStatus(Long matchingId);
    public void checkAlreadyJoin(Long matchingId, Long userId);
    public void checkMaxNum(Matching matching, PersonalMatchingTeam personalMatchingTeam);
}
