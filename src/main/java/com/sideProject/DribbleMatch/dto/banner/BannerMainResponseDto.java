package com.sideProject.DribbleMatch.dto.banner;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerMainResponseDto {

    private String imagePath;
    private Long postId;

    @Builder
    public BannerMainResponseDto(String imagePath, Long postId) {
        this.imagePath = imagePath;
        this.postId = postId;
    }
}
