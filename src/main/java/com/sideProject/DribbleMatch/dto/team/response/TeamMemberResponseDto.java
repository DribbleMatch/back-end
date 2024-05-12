package com.sideProject.DribbleMatch.dto.team.response;

import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMemberResponseDto {
    Long memberId;
    String name;
    LocalDateTime joinAt;

    @Builder
    public TeamMemberResponseDto(Long memberId, String name, LocalDateTime joinAt) {
        this.memberId = memberId;
        this.name = name;
        this.joinAt = joinAt;
    }

    public TeamMemberResponseDto toDto(TeamMember teamMember) {
        return TeamMemberResponseDto.builder()
                .memberId(teamMember.getUser().getId())
                .name(teamMember.getUser().getNickName())
                .joinAt(teamMember.getCreatedAt())
                .build();
    }
}
