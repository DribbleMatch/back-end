package com.sideProject.ClutchShot.repository.personalMatchJoin;

import com.sideProject.ClutchShot.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.ClutchShot.entity.personalMatchJoin.PersonalMatchJoin;
import com.sideProject.ClutchShot.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface PersonalMatchJoinCustomRepository {
    public Long countPersonalMatchJoinByMatchingAndTeam(Long matchingId, PersonalMatchingTeam matchingTeam);
    public List<User> findUserByMatchingAndTeam(Long matchingId, PersonalMatchingTeam personalMatchingTeam);
    public Optional<PersonalMatchJoin> findByMatchingIdAndUserId(Long matchingId, Long userId);
}
