package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.IsReservedStadium;
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

    private Long id;
    private LocalTime startAt;
    private String name;
    private GameKind isOnlyWomen;
    private GameKind gameKind;
    private int playMemberNum;
    private int maxMemberNum;
    private String regionString;
    private IsReservedStadium isReservedStadium;
    private int hour;
    private int upTeamMemberNum;
    private int downTeamMemberNum;


    @Builder
    public MatchingResponseDto(
            Long id,
            LocalTime startAt,
            String name,
            GameKind isOnlyWomen,
            GameKind gameKind,
            int playMemberNum,
            int maxMemberNum,
            String regionString,
            IsReservedStadium isReservedStadium,
            int hour,
            int upTeamMemberNum,
            int downTeamMemberNum
    ) {
        this.id = id;
        this.startAt = startAt;
        this.name = name;
        this.isOnlyWomen = isOnlyWomen;
        this.gameKind = gameKind;
        this.playMemberNum = playMemberNum;
        this.maxMemberNum = maxMemberNum;
        this.regionString = regionString;
        this.isReservedStadium = isReservedStadium;
        this.hour = hour;
        this.upTeamMemberNum = upTeamMemberNum;
        this.downTeamMemberNum = downTeamMemberNum;
    }
}
