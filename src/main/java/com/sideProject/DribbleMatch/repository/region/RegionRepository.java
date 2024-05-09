package com.sideProject.DribbleMatch.repository.region;

import com.sideProject.DribbleMatch.entity.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>, RegionCustomRepository {
}
