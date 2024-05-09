package com.sideProject.DribbleMatch.service.region;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService{

    private final RegionRepository regionRepository;

    @Override
    public Region findRegion(String regionString) {
        return regionRepository.findByRegionString(regionString).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
    }

    @Override
    public String findRegionString(Long regionId) {
        return regionRepository.findRegionStringById(regionId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_ID));
    }

    @Override
    public List<Long> findRegionIds(String regionString) {
        return regionRepository.findIdsByRegionString(regionString);
    }
}
