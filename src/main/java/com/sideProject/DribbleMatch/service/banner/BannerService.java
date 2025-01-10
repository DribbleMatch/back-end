package com.sideProject.DribbleMatch.service.banner;

import com.sideProject.DribbleMatch.dto.banner.BannerMainResponseDto;
import com.sideProject.DribbleMatch.dto.post.request.PostCreateRequestDto;
import com.sideProject.DribbleMatch.entity.Post.Post;

import java.util.List;

public interface BannerService {

    public Long createBanner(PostCreateRequestDto requestDto, Long postId);
    public List<BannerMainResponseDto> getMainPageBannerList();
}
