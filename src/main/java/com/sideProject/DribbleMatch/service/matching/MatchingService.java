package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.MatchingResponseDto;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MatchingService {
    public Long createMatching(MatchingCreateRequestDto request);
    public Long updateMatching(Long matchingId, MatchingUpdateRequestDto request);
    public String deleteMatching(Long matchingId);
    public Page<MatchingResponseDto> findAllMatchings(Pageable pageable, String regionString);
    public MatchingResponseDto findMatching(Long matchingId);

    public List<Matching> findAllMatchingOrderByTime(LocalDate localDate);

}
