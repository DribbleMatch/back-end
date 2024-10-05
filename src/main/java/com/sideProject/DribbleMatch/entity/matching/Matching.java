package com.sideProject.DribbleMatch.entity.matching;

import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.IsReservedStadium;
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

    @NotNull
    @Column
    private GameKind gameKind;

    @NotNull
    @Column
    private GameKind isOnlyWomen;

    @NotNull
    @Column
    private IsReservedStadium isReserved;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column
    private String jibun;

    @Column
    private String stadiumLoadAddress;

    @Column
    private String detailAddress;


    @Builder
    protected Matching(
            String name,
            int playPeople,
            int maxPeople,
            LocalDateTime startAt,
            int hour,
            GameKind gameKind,
            GameKind isOnlyWomen,
            IsReservedStadium isReserved,
            Region region,
            String jibun,
            String stadiumLoadAddress,
            String detailAddress) {
        this.name = name;
        this.playPeople = playPeople;
        this.maxPeople = maxPeople;
        this.startAt = startAt;
        this.hour = hour;
        this.status = MatchingStatus.RECRUITING;
        this.gameKind = gameKind;
        this.isOnlyWomen = isOnlyWomen;
        this.isReserved = isReserved;
        this.region = region;
        this.jibun = jibun;
        this.stadiumLoadAddress = stadiumLoadAddress;
        this.detailAddress = detailAddress;
    }
}
