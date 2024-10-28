package com.sideProject.DribbleMatch.dto.team.response;

import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMemberResponseDto {
    Long memberId;
    String nickname;
    TeamRole role;
    LocalDateTime joinAt;

    @Builder
    public TeamMemberResponseDto(Long memberId, String nickname, LocalDateTime joinAt, TeamRole role) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.joinAt = joinAt;
        this.role = role;
    }

    public static TeamMemberResponseDto toDto(TeamMember teamMember) {
        return TeamMemberResponseDto.builder()
                .memberId(teamMember.getUser().getId())
                .nickname(teamMember.getUser().getNickName())
                .joinAt(teamMember.getCreatedAt())
                .role(teamMember.getTeamRole())
                .build();
    }
}
