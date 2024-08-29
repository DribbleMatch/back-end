package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingResponseDto {

    private String name;
    private String imgPath;
    private String stadiumName;
    private int playPeople;
    private LocalDateTime startAt;
    private int hour;
    private GameKind gameKind;
    private MatchingStatus status;

    private int maxPeople;
    private String regionString;

    @Builder
    public MatchingResponseDto(
        String name,
        String imgPath,
        String stadiumName,
        int playPeople,
        LocalDateTime startAt,
        int hour,
        GameKind gameKind,
        MatchingStatus status,
        int maxPeople,
        String regionString
    ) {
        this.name = name;
        this.imgPath = imgPath;
        this.stadiumName = stadiumName;
        this.playPeople = playPeople;
        this.startAt = startAt;
        this.hour = hour;
        this.gameKind = gameKind;
        this.status = status;
        this.maxPeople = maxPeople;
        this.regionString = regionString;
    }

    public static MatchingResponseDto of(Matching matching, String regionString) {
        return MatchingResponseDto.builder()
                .name(matching.getName())
                .imgPath(matching.getImgPath())
                .stadiumName(matching.getStadium() == null ? null : matching.getStadium().getName())
                .playPeople(matching.getPlayPeople())
                .startAt(matching.getStartAt())
                .hour(matching.getHour())
                .gameKind(matching.getGameKind())
                .status(matching.getStatus())
                .maxPeople(matching.getMaxPeople())
                .regionString(regionString)
                .build();
    }
}
