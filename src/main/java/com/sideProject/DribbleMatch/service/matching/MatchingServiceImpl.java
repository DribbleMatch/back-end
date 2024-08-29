package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.MatchingResponseDto;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import com.sideProject.DribbleMatch.repository.matching.MatchingRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.stadium.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingServiceImpl implements MatchingService{

    private final RegionRepository regionRepository;
    private final StadiumRepository stadiumRepository;
    private final MatchingRepository matchingRepository;

    public Long createMatching(MatchingCreateRequestDto requestDto) {

        Stadium stadium = null;
        Region region = null;

        if (!requestDto.getStadiumString().isEmpty()) {
            stadium = stadiumRepository.findByName(requestDto.getStadiumString()).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_STADIUM_NAME));
        }

        if (!requestDto.getRegionString().isEmpty()) {
            region = regionRepository.findByRegionString(requestDto.getRegionString()).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
        }

        return matchingRepository.save(MatchingCreateRequestDto.of(requestDto, stadium, region)).getId();
    }
    public Long updateMatching(Long matchingId, MatchingUpdateRequestDto request) {
        return null;
    }
    public String deleteMatching(Long matchingId) {
        return null;
    }
    public Page<MatchingResponseDto> findAllMatchings(Pageable pageable, String regionString) {
        return null;
    }
    public MatchingResponseDto findMatching(Long matchingId) {
        return null;
    }

    public List<Matching> findAllMatchingOrderByTime(LocalDate localDate) {
        return matchingRepository.findByStartDateOrderByStartTime(localDate);
    }
}
