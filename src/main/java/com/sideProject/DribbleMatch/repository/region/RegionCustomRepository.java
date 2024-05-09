package com.sideProject.DribbleMatch.repository.region;

import com.sideProject.DribbleMatch.entity.region.Region;

import java.util.List;
import java.util.Optional;

public interface RegionCustomRepository {

    public Optional<Region> findByRegionString(String regionString);
    public Optional<String> findRegionStringById(Long regionId);
    public List<Long> findIdsByRegionString(String regionString);
}
