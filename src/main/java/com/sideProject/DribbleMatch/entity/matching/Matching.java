package com.sideProject.DribbleMatch.entity.matching;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
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
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @Column
    private int playPeople;

    @Column
    private int maxPeople;

    @NotNull
    @Column
    private LocalDateTime startAt;

    @NotNull
    @Column
    private int hour;

    @NotNull
    @Column
    private MatchingStatus status;

    @ManyToOne
    @JoinColumn(name = "region_id")
    @NotNull
    private Region region;

    @OneToOne
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    private GameKind gameKind;

    private String imgPath;

    @Builder
    protected Matching(
            String name,
            int playPeople,
            int maxPeople,
            LocalDateTime startAt,
            int hour,
            MatchingStatus status,
            Region region,
            Stadium stadium,
            GameKind gameKind,
            String imgPath) {
        this.name = name;
        this.playPeople = playPeople;
        this.maxPeople = maxPeople;
        this.startAt = startAt;
        this.hour = hour;
        this.status = status;
        this.region = region;
        this.stadium = stadium;
        this.gameKind = gameKind;
        this.imgPath = imgPath;
    }

    public void updateMatching(MatchingUpdateRequestDto request, Region region) {
        this.name = name;
        this.playPeople = request.getPlayPeople();
        this.maxPeople = request.getMaxPeople();
        this.startAt = request.getStartAt();
        this.hour = request.getHour();
        this.region = region;
    }
}
