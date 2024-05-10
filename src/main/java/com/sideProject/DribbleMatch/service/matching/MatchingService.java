package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.MatchingResponstDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;

public interface MatchingService {
    public Long createMatching(MatchingCreateRequestDto request);
    public Long updateMatching(MatchingUpdateRequestDto request);
    public String deleteMatching(Long matchingId);
    public String EnterMatchingStadiumInfo();
    public MatchingResponstDto findMatching(Long matchingId);
    public Page<MatchingResponstDto> findAllMatchings(Pageable pageable, String regionString);

}
