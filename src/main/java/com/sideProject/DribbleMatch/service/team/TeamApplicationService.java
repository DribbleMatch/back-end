package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.User;

public interface TeamApplicationService {
    // requestId: 요청하는 사람 (현재 로그인 중인 유저). 팀 지원 요청이면 requestId: userId, 팀 지원 요청 승인이면 requestId: adminId
    public Long apply(Long requestId, TeamJoinRequestDto request);
    public Long approve(Long userId, Long teamApplicationId);
    public Long refuse(Long userId, Long teamApplicationId);
}
