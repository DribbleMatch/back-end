package com.sideProject.ClutchShot.repository.teamMember;

import com.sideProject.ClutchShot.entity.teamMember.TeamMember;

import java.util.List;

public interface TeamMemberCustomRepository {

    public List<String> findTeamNameByUserId(Long userId);
    public List<TeamMember> findAllByTeamName(String teamName);
}
