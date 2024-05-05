package com.sideProject.DribbleMatch.service.region;

import com.sideProject.DribbleMatch.entity.region.Region;

import java.util.List;

public interface RegionService {
    public Region findRegion(String regionString);
    public String findRegionString(Long regionId);
    public List<Long> findRegionIds(String regionString);
}
