package com.sideProject.DribbleMatch.repository.banner;

import com.sideProject.DribbleMatch.entity.banner.Banner;

import java.util.List;

public interface BannerCustomRepository {

    public List<Banner> selectMainBannerList();
}
