package com.sideProject.DribbleMatch.entity.matching;

import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Matching {

    @NotNull
    @Column
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
    private LocalDateTime endAt;

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

    protected Matching(
            String name,
            int playPeople,
            int maxPeople,
            LocalDateTime startAt,
            LocalDateTime endAt,
            MatchingStatus status,
            Region region,
            Stadium stadium) {
        this.name = name;
        this.playPeople = playPeople;
        this.maxPeople = maxPeople;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.region = region;
        this.stadium = stadium;
    }

    public void update(String name, int playPeople, int maxPeople, LocalDateTime startAt, LocalDateTime endAt, Region region) {
        this.name = name;
        this.playPeople = playPeople;
        this.maxPeople = maxPeople;
        this.startAt = startAt;
        this.endAt = endAt;
        this.region = region;
    }
}
