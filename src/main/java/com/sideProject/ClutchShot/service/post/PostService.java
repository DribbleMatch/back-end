package com.sideProject.ClutchShot.service.post;

import com.sideProject.ClutchShot.dto.post.request.PostCreateRequestDto;
import com.sideProject.ClutchShot.dto.post.response.PostDetailResponseDto;
import com.sideProject.ClutchShot.dto.post.response.PostListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    public Long createPost(PostCreateRequestDto requestDto);
    public void plusViewCount(Long postId);
    public PostDetailResponseDto getPostDetail(Long postId);
    public Page<PostListResponseDto> getPostList(Pageable pageable);
}
