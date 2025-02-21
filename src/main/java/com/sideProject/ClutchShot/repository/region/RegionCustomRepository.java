package com.sideProject.ClutchShot.repository.region;

import com.sideProject.ClutchShot.entity.region.Region;

import java.util.List;
import java.util.Optional;

public interface RegionCustomRepository {

    public Optional<Region> findByRegionString(String regionString);
    public Optional<String> findRegionStringById(Long regionId);
    public List<String> findAllSiDo();
    public List<String> findAllSiGunGuBySiDo(String siDo);
    public List<Long> findIdsByRegionString(String regionString);
}
