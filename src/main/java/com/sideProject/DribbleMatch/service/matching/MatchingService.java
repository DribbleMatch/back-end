package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingInputScoreRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.*;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MatchingService {
    public Long createMatching(MatchingCreateRequestDto request, Long creatorId);
    public Page<MatchingResponseDto> searchMatchings(String searchWord, Pageable pageable, LocalDate date);
    public MatchingDetailResponseDto getMatchingDetail(Long matchingId);
    public Page<ReservedMatchingResponseDto> getReservedMatchingList(Long userId, GameKind gameKind, Pageable pageable);
    public Page<EndedMatchingResponseDto> getEndedMatchingList(Long userId, GameKind gameKind, Pageable pageable);
    public Boolean checkHasNotInputScore(Long userId);
    public List<NotInputScoreMatchingResponseDto> getNotInputScoreMatchingList(Long userId);
    public void inputScore(MatchingInputScoreRequestDto requestDto);
    public void notPlayMatching(Long matchingId);
    public List<RecentMatchingResponseDto> getRecentMatchingList();
}
