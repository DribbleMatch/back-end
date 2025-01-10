package com.sideProject.DribbleMatch.dto.post.response;

import com.sideProject.DribbleMatch.entity.Post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailResponseDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private int viewCount;
    private String content;

    private Long beforeId;
    private String beforeTitle;
    private LocalDateTime beforeCreatedAt;
    private Long afterId;
    private String afterTitle;
    private LocalDateTime afterCreatedAt;

    @Builder
    public PostDetailResponseDto(Long id,
                                 String title,
                                 LocalDateTime createdAt,
                                 int viewCount,
                                 String content,
                                 Post beforePost,
                                 Post afterPost) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.content = content;

        if (beforePost != null) {
            this.beforeId = beforePost.getId();
            this.beforeTitle = beforePost.getTitle();
            this.beforeCreatedAt = beforePost.getCreatedAt();
        }

        if (afterPost != null) {
            this.afterId = afterPost.getId();
            this.afterTitle = afterPost.getTitle();
            this.afterCreatedAt = afterPost.getCreatedAt();
        }
    }
}
