package com.sideProject.DribbleMatch.service.componentService.BannerPost;

import com.sideProject.DribbleMatch.dto.post.request.PostCreateRequestDto;

public interface BannerPostService {
    public Long createPost(PostCreateRequestDto requestDto);
}
