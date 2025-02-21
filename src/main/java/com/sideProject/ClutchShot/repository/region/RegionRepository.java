package com.sideProject.ClutchShot.repository.region;

import com.sideProject.ClutchShot.entity.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>, RegionCustomRepository {
}
