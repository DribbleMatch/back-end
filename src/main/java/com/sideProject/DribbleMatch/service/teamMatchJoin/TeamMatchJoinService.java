package com.sideProject.DribbleMatch.service.teamMatchJoin;

public interface TeamMatchJoinService {
    public void createTeamMatchJoin(Long matchingId, Long userId, String teamName);

    public void checkAlreadyJoin(Long matchingId, Long userId);
}
