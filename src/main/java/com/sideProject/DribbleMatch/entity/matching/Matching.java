package com.sideProject.DribbleMatch.entity.matching;

import com.sideProject.DribbleMatch.entity.BaseEntity;
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
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    private String name;

    @Column
    private int playNum;

    @Column
    private int maxNum;

    @NotNull
    @Column
    private LocalDateTime startAt;

    @NotNull
    @Column
    private LocalDateTime endAt;

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

    @Column
    private int upTeamScore;

    @Column
    private int downTeamScore;

    @Column
    private String upTeamName;

    @Column
    private String downTeamName;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;


    @Builder
    protected Matching(
            String name,
            int playNum,
            int maxNum,
            LocalDateTime startAt,
            LocalDateTime endAt,
            int hour,
            GameKind gameKind,
            GameKind isOnlyWomen,
            IsReservedStadium isReserved,
            Region region,
            String jibun,
            String stadiumLoadAddress,
            String detailAddress,
            int upTeamScore,
            int downTeamScore,
            String upTeamName,
            String downTeamName,
            User creator
            ) {
        this.name = name;
        this.playNum = playNum;
        this.maxNum = maxNum;
        this.startAt = startAt;
        this.endAt = endAt;
        this.hour = hour;
        this.status = MatchingStatus.RECRUITING;
        this.gameKind = gameKind;
        this.isOnlyWomen = isOnlyWomen;
        this.isReserved = isReserved;
        this.region = region;
        this.jibun = jibun;
        this.stadiumLoadAddress = stadiumLoadAddress;
        this.detailAddress = detailAddress;
        this.upTeamScore = upTeamScore;
        this.downTeamScore = downTeamScore;
        this.upTeamName = upTeamName;
        this.downTeamName = downTeamName;
        this.creator = creator;
    }

    public void inputScore(int upTeamScore, int downTeamScore) {
        this.upTeamScore = upTeamScore;
        this.downTeamScore = downTeamScore;
        this.status = MatchingStatus.FINISHED;
    }

    public void updateStatus(MatchingStatus status) {
        this.status = status;
    }

    public void joinTeamMatching(String teamName) {
        this.downTeamName = teamName;
    }
}
