package com.sideProject.ClutchShot.repository.banner;

import com.sideProject.ClutchShot.entity.banner.Banner;

import java.util.List;

public interface BannerCustomRepository {

    public List<Banner> selectMainBannerList();
}
