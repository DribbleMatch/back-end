package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.MatchingResponseDto;
import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.repository.matching.personalMatching.PersonalMatchingRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

//todo: 경기장이 미정일 경우 경기장이 확정되면 경기장을 입력하는데, Matching에서 처리 VS Stadium에서 처리
@Service
@RequiredArgsConstructor
public class PersonalMatchingServiceImpl implements MatchingService{

    private final RegionRepository regionRepository;
    private final PersonalMatchingRepository personalMatchingRepository;

    @Override
    public Long createMatching(MatchingCreateRequestDto request) {

        checkUniqueName(request.getName());

        Region region = regionRepository.findByRegionString(request.getRegionString()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
        PersonalMatching personalMatching = personalMatchingRepository.save(MatchingCreateRequestDto.toEntity(request, region));
        return personalMatching.getId();
    }

    @Override
    public Long updateMatching(Long personalMatchingId, MatchingUpdateRequestDto request) {

        checkUniqueName(request.getName());

        Region region = regionRepository.findByRegionString(request.getRegionString()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
        PersonalMatching personalMatchingToUpdate = personalMatchingRepository.findById(personalMatchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_PERSONAL_MATCHING_ID));

        personalMatchingToUpdate.updateMatching(request, region);

        return personalMatchingId;
    }

    @Override
    public String deleteMatching(Long matchingId) {

        personalMatchingRepository.deleteById(matchingId);

        return "개인 경기가 삭제되었습니다.";
    }

    @Override
    public MatchingResponseDto findMatching(Long matchingId) {

        PersonalMatching personalMatching = personalMatchingRepository.findById(matchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_PERSONAL_MATCHING_ID));

        String regionString = regionRepository.findRegionStringById(personalMatching.getRegion().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_ID));

        return MatchingResponseDto.of(personalMatching, regionString);
    }

    @Override
    public Page<MatchingResponseDto> findAllMatchings(Pageable pageable, String regionString) {

        if(regionString.equals("ALL")) {
            Page<PersonalMatching> personalMatchings = personalMatchingRepository.findAll(pageable);

            return personalMatchings
                    .map(personalMatching -> MatchingResponseDto.of(personalMatching, regionRepository.findRegionStringById(personalMatching.getRegion().getId()).orElseThrow(() ->
                            new CustomException(ErrorCode.NOT_FOUND_REGION_ID))));
        }

        List<Long> regionIds = regionRepository.findIdsByRegionString(regionString);
        Page<PersonalMatching> personalMatchings = personalMatchingRepository.findByRegionIds(pageable, regionIds);

        return personalMatchings
                .map(personalMatching -> MatchingResponseDto.of(personalMatching, regionRepository.findRegionStringById(personalMatching.getRegion().getId()).orElseThrow(() ->
                        new CustomException(ErrorCode.NOT_FOUND_REGION_STRING))));
    }

    //todo: 매칭 종료되면 선수들 득점, 리바운드 등 기입 (심판이)

    private void checkUniqueName(String name) {
        if(personalMatchingRepository.findByName(name).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_PERSONAL_MATCHING_NAME);
        }
    }
}
