package com.sideProject.DribbleMatch.service.componentService.BannerPost;

import com.sideProject.DribbleMatch.dto.post.request.PostCreateRequestDto;
import com.sideProject.DribbleMatch.entity.Post.Post;
import com.sideProject.DribbleMatch.service.banner.BannerService;
import com.sideProject.DribbleMatch.service.post.PostService;
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
