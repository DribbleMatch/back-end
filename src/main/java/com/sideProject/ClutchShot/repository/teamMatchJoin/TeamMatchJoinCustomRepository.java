package com.sideProject.ClutchShot.repository.teamMatchJoin;

import com.sideProject.ClutchShot.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.ClutchShot.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface TeamMatchJoinCustomRepository {
    public Long countTeamMatchJoinByMatchingIdAndTeamName(Long matchingId, String teamName);
    public Optional<TeamMatchJoin> findByMatchingIdAndUserId(Long matchingId, Long userId);
    public List<User> findAllUsersByMatchingIdAndTeamName(Long matchingId, String teamName);
}
