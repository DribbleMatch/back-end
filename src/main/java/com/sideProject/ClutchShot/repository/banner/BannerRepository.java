package com.sideProject.ClutchShot.repository.banner;

import com.sideProject.ClutchShot.entity.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner,Integer>, BannerCustomRepository {
}
