package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class TeamMatchingResponseDto {
    private Long id;
    private String name;
    private int playPeople;
    private int maxTeam;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private MatchingStatus status;
    private String regionString;
    private String stadiumString;
    private List<TeamResponseDto> teams;

    @Builder
    public TeamMatchingResponseDto(
            Long id,
            String name,
            int playPeople,
            int maxTeam,
            LocalDateTime startAt,
            LocalDateTime endAt,
            MatchingStatus status,
            String regionString,
            String stadiumString,
            List<TeamResponseDto> teams
    ) {
        this.id = id;
        this.name = name;
        this.playPeople = playPeople;
        this.maxTeam = maxTeam;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.regionString = regionString;
        this.stadiumString = stadiumString;
        this.teams = teams;
    }

    public static TeamMatchingResponseDto of(
            TeamMatch matching,
            String regionString,
            String stadiumString,
            List<TeamResponseDto> teams

    ) {
        return TeamMatchingResponseDto.builder()
                .name(matching.getName())
                .playPeople(matching.getPlayPeople())
                .maxTeam(matching.getMaxTeam())
                .startAt(matching.getStartAt())
                .endAt(matching.getEndAt())
                .status(matching.getStatus())
                .regionString(regionString)
                .stadiumString(stadiumString)
                .teams(teams)
                .build();
    }
}
