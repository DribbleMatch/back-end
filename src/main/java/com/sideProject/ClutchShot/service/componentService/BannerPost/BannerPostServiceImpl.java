package com.sideProject.ClutchShot.service.componentService.BannerPost;

import com.sideProject.ClutchShot.dto.post.request.PostCreateRequestDto;
import com.sideProject.ClutchShot.service.banner.BannerService;
import com.sideProject.ClutchShot.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerPostServiceImpl implements BannerPostService {

    private final BannerService bannerService;
    private final PostService postService;

    @Override
    @Transactional
    public Long createPost(PostCreateRequestDto requestDto) {

        Long postId = postService.createPost(requestDto);
        bannerService.createBanner(requestDto, postId);

        return postId;
    }
}
