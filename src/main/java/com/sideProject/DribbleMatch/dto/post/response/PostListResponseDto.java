package com.sideProject.DribbleMatch.dto.post.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListResponseDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt;

    @Builder
    public PostListResponseDto(Long id, String title, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}
