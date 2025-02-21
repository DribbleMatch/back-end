package com.sideProject.ClutchShot.service.post;

import com.sideProject.ClutchShot.dto.post.request.PostCreateRequestDto;
import com.sideProject.ClutchShot.dto.post.response.PostDetailResponseDto;
import com.sideProject.ClutchShot.dto.post.response.PostListResponseDto;
import com.sideProject.ClutchShot.entity.Post.ENUM.PostStatus;
import com.sideProject.ClutchShot.entity.Post.Post;
import com.sideProject.ClutchShot.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Long createPost(PostCreateRequestDto requestDto) {
        return postRepository.save(Post.builder()
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .status(requestDto.getStatus())
                    .hasBanner(requestDto.getHasBanner())
                .build()).getId();
    }

    @Override
    @Transactional
    public void plusViewCount(Long postId) {
        postRepository.plusViewCount(postId);
    }

    @Override
    public PostDetailResponseDto getPostDetail(Long postId) {

        List<Post> postList = postRepository.searchPostsPreNowNextById(postId);

        Post beforePost = postList.get(0);
        Post currentPost = postList.get(1);
        Post afterPost = postList.get(2);

        return PostDetailResponseDto.builder()
                .id(currentPost.getId())
                .title(currentPost.getTitle())
                .createdAt(currentPost.getCreatedAt())
                .viewCount(currentPost.getViewCount())
                .content(currentPost.getContent())
                .beforePost(beforePost)
                .afterPost(afterPost)
                .build();

        // 이전글 다음글 확인. 조회수 확인
    }

    @Override
    public Page<PostListResponseDto> getPostList(Pageable pageable) {

        Page<Post> postPage = postRepository.findByStatus(pageable, PostStatus.SHOW);

        List<PostListResponseDto> responseList = postPage.stream()
                .map(post -> PostListResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, postPage.getTotalElements());
    }
}
