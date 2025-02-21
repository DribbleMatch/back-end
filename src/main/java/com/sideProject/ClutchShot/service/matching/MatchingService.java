package com.sideProject.ClutchShot.service.matching;

import com.sideProject.ClutchShot.dto.matching.request.MatchingInputScoreRequestDto;
import com.sideProject.ClutchShot.dto.matching.response.*;
import com.sideProject.ClutchShot.entity.matching.ENUM.GameKind;
import com.sideProject.ClutchShot.entity.matching.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sideProject.ClutchShot.dto.matching.request.MatchingCreateRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface MatchingService {
    public Long createMatching(MatchingCreateRequestDto request, Long creatorId);
    public Long changeDownTeamName(Matching matching, String teamName);
    public void inputScore(MatchingInputScoreRequestDto requestDto);
    public void notPlayMatching(Long matchingId);
    public Boolean checkHasNotInputScore(Long userId);
    public List<MatchingSimpleResponseDto> getNotInputScoreMatchingList(Long userId);
    public List<RecentMatchingResponseDto> getRecentMatchingList();
    public Page<MatchingSimpleResponseDto> searchMatchings(String searchWord, Pageable pageable, LocalDate date);
    public MatchingUserDetailResponseDto getMatchingDetail(Long matchingId);
    public Page<MatchingDetailTestResponseDto> getReservedMatchingList(Long userId, GameKind gameKind, Pageable pageable);
    public Page<MatchingDetailTestResponseDto> getEndedMatchingList(Long userId, GameKind gameKind, Pageable pageable);
}
