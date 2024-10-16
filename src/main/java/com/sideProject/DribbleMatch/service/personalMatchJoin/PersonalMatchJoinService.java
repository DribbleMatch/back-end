package com.sideProject.DribbleMatch.service.personalMatchJoin;

import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;

public interface PersonalMatchJoinService {

    public void createPersonalMatchJoin(Long matchingId, Long userId, PersonalMatchingTeam personalMatchingTeam);
    public void checkAlreadyJoin(Long matchingId, Long userId);
}
