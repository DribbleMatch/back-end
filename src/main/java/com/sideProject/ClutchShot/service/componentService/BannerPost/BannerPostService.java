package com.sideProject.ClutchShot.service.componentService.BannerPost;

import com.sideProject.ClutchShot.dto.post.request.PostCreateRequestDto;

public interface BannerPostService {
    public Long createPost(PostCreateRequestDto requestDto);
}
