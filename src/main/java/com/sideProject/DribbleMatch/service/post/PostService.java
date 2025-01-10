package com.sideProject.DribbleMatch.service.post;

import com.sideProject.DribbleMatch.dto.post.request.PostCreateRequestDto;
import com.sideProject.DribbleMatch.dto.post.response.PostDetailResponseDto;
import com.sideProject.DribbleMatch.dto.post.response.PostListResponseDto;
import com.sideProject.DribbleMatch.entity.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    public Long createPost(PostCreateRequestDto requestDto);
    public void plusViewCount(Long postId);
    public PostDetailResponseDto getPostDetail(Long postId);
    public Page<PostListResponseDto> getPostList(Pageable pageable);
}
