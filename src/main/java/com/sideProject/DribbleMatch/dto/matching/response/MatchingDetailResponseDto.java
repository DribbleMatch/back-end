package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.IsReservedStadium;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingDetailResponseDto {

    private Long id;
    private String name;
    private IsReservedStadium isReservedStadium;
    private String regionString;
    private int playMemberNum;
    private int maxMemberNum;
    private GameKind isOnlyWomen;
    private GameKind gameKind;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int hour;
    private String aTeamName;
    private String bTeamName;
    private List<MatchingUserResponseDto> aTeamMember;
    private List<MatchingUserResponseDto> bTeamMember;


    @Builder
    public MatchingDetailResponseDto(
            Long id,
            String name,
            IsReservedStadium isReservedStadium,
            String regionString,
            int playMemberNum,
            int maxMemberNum,
            GameKind isOnlyWomen,
            GameKind gameKind,
            LocalDateTime startAt,
            LocalDateTime endAt,
            int hour,
            String aTeamName,
            String bTeamName,
            List<MatchingUserResponseDto> aTeamMember,
            List<MatchingUserResponseDto> bTeamMember
    ) {
        this.id = id;
        this.name = name;
        this.isReservedStadium = isReservedStadium;
        this.regionString = regionString;
        this.playMemberNum = playMemberNum;
        this.maxMemberNum = maxMemberNum;
        this.isOnlyWomen = isOnlyWomen;
        this.gameKind = gameKind;
        this.startAt = startAt;
        this.endAt = endAt;
        this.hour = hour;
        this.aTeamName = aTeamName;
        this.bTeamName = bTeamName;
        this.aTeamMember = aTeamMember;
        this.bTeamMember = bTeamMember;
    }
}
