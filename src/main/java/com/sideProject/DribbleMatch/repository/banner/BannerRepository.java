package com.sideProject.DribbleMatch.repository.banner;

import com.sideProject.DribbleMatch.entity.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner,Integer>, BannerCustomRepository {
}
