package com.sideProject.DribbleMatch.dto.post.request;

import com.sideProject.DribbleMatch.entity.Post.ENUM.HasBanner;
import com.sideProject.DribbleMatch.entity.Post.ENUM.PostStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateRequestDto {

    @NotNull(message = "공지 제목이 입력되지 않았습니다.")
    private String title;

    @NotNull(message = "공지 내용이 입력되지 않았습니다.")
    private String content;

    @NotNull(message = "공개여부가 입력되지 않았습니다.")
    private PostStatus status;

    @NotNull(message = "배너유무가 입력되지 않았습니다.")
    private HasBanner hasBanner;

    private LocalDate bannerStartAt;

    private LocalDate bannerEndAt;

    private MultipartFile bannerImage;

    @Builder
    public PostCreateRequestDto(String title,
                                String content,
                                PostStatus status,
                                HasBanner hasBanner,
                                LocalDate bannerStartAt,
                                LocalDate bannerEndAt,
                                MultipartFile bannerImage) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.hasBanner = hasBanner;
        this.bannerStartAt = bannerStartAt;
        this.bannerEndAt = bannerEndAt;
        this.bannerImage = bannerImage;
    }
}
