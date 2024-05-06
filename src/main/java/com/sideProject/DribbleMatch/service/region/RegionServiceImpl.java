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
        List<String> parsedRegionString = parseToRegion(regionString);
        return regionRepository.findByRegionString(parsedRegionString.get(0), parsedRegionString.get(1), parsedRegionString.get(2), parsedRegionString.get(3), parsedRegionString.get(4)).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
    }

    @Override
    public String findRegionString(Long regionId) {
        return regionRepository.findRegionStringById(regionId).trim();
    }

    @Override
    public List<Long> findRegionIds(String regionString) {
        List<String> parsedRegionString = parseToRegion(regionString);
        return regionRepository.findIdsByRegionString(parsedRegionString.get(0), parsedRegionString.get(1), parsedRegionString.get(2), parsedRegionString.get(3), parsedRegionString.get(4));
    }

    private List<String> parseToRegion(String regionString) {
        List<String> parsedRegionString = new ArrayList<>(Arrays.stream(regionString.split(" ")).toList());
        while (parsedRegionString.size() < 5) {
            parsedRegionString.add(null);
        }
        return parsedRegionString;
    }
}
