package com.sideProject.DribbleMatch.repository.teamMember;

import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;

import java.util.List;

public interface TeamMemberCustomRepository {

    public List<String> findTeamNameByUserIdAndAdmin(Long userId);
    public List<TeamMember> findAllByTeamName(String teamName);
}
