package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.TeamMatchingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamMatchingService {
    public Long createMatching(TeamMatchingCreateRequestDto request, Long teamId, Long userId);
    public Long updateMatching(TeamMatchingUpdateRequestDto request, Long matchingId, Long userId);
    public Long deleteMatching(Long matchingId, Long userId);
    public String joinMatching(Long matchingId, Long teamId, Long userId);
    public TeamMatchingResponseDto findMatching(Long id);
    public Page<TeamMatchingResponseDto> findMatching(Pageable pageable, String sido);
}
