package com.sideProject.ClutchShot.service.banner;

import com.sideProject.ClutchShot.dto.banner.BannerMainResponseDto;
import com.sideProject.ClutchShot.dto.post.request.PostCreateRequestDto;

import java.util.List;

public interface BannerService {

    public Long createBanner(PostCreateRequestDto requestDto, Long postId);
    public List<BannerMainResponseDto> getMainPageBannerList();
}
