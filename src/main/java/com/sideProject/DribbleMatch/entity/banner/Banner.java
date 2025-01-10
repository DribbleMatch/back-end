package com.sideProject.DribbleMatch.entity.banner;

import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.Post.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotNull
    private String imagePath;

    @OneToOne
    @JoinColumn(name = "post_id")
    @NotNull
    private Post post;

    @Column
    @NotNull
    private LocalDate startAt;

    @Column
    @NotNull
    private LocalDate endAt;

    @Builder
    protected Banner(
            String imagePath,
            Post post,
            LocalDate startAt,
            LocalDate endAt
            ) {
        this.imagePath = imagePath;
        this.post = post;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
