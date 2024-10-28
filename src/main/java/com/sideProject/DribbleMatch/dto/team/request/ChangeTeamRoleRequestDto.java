package com.sideProject.DribbleMatch.dto.team.request;

import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangeTeamRoleRequestDto {

    private Long teamMemberId;
    private TeamRole teamRole;

    @Builder
    public ChangeTeamRoleRequestDto(Long teamMemberId, TeamRole teamRole) {
        this.teamMemberId = teamMemberId;
        this.teamRole = teamRole;
    }
}
