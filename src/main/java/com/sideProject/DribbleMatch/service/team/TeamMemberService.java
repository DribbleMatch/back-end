package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;

public interface TeamMemberService {
    public Long join(TeamJoinRequestDto request, Long userId);
    public Long withdraw(Long memberId, Long teamId, Long adminId);
}
