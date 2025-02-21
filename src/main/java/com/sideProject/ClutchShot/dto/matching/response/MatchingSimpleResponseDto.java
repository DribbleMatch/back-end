package com.sideProject.ClutchShot.dto.matching.response;

import com.sideProject.ClutchShot.entity.matching.ENUM.GameKind;
import com.sideProject.ClutchShot.entity.matching.ENUM.IsReservedStadium;
import com.sideProject.ClutchShot.entity.matching.ENUM.MatchingStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingSimpleResponseDto {

    private Long id;
    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int hour;
    private GameKind gameKind;
    private GameKind isOnlyWomen;
    private IsReservedStadium isReservedStadium;
    private int playMemberNum;
    private int maxMemberNum;
    private String regionString;
    private String upTeamName;
    private String downTeamName;
    private MatchingStatus status;
}
