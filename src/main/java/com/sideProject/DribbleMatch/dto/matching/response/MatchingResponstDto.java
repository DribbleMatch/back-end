package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingResponstDto {

    private String name;
    private int playPeople;
    private int maxPeople;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private MatchingStatus status;
    private String regionString;
    private String stadiumString;

    @Builder
    public MatchingResponstDto(
            String name,
            int playPeople,
            int maxPeople,
            LocalDateTime startAt,
            LocalDateTime endAt,
            MatchingStatus status,
            String regionString,
            String stadiumString
    ) {
        this.name = name;
        this.playPeople = playPeople;
        this.maxPeople = maxPeople;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.regionString = regionString;
        this.stadiumString = stadiumString;
    }

    public MatchingResponstDto of(Matching matching, String regionString, String stadiumString) {
        return MatchingResponstDto.builder()
                .name(matching.getName())
                .playPeople(matching.getPlayPeople())
                .maxPeople(matching.getMaxPeople())
                .startAt(matching.getStartAt())
                .endAt(matching.getEndAt())
                .status(matching.getStatus())
                .regionString(regionString)
                .stadiumString(stadiumString)
                .build();
    }
}
