package com.sideProject.DribbleMatch.entity.Post;

import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.Post.ENUM.HasBanner;
import com.sideProject.DribbleMatch.entity.Post.ENUM.PostStatus;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.IsReservedStadium;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column(unique = true)
    private String title;

    @NotNull
    @Column
    private PostStatus status;

    @NotNull
    @Column
    private HasBanner hasBanner;

    @Column
    private int viewCount;

    @Column(length = 10000)
    private String content;



    @Builder
    protected Post(String title,
                   PostStatus status,
                   HasBanner hasBanner,
                   String content
            ) {
        this.title = title;
        this.status = status;
        this.hasBanner = hasBanner;
        this.content = content;
    }
}
