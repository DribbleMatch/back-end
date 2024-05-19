package com.sideProject.DribbleMatch.dto.matching.request;

import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.matching.teamMatch;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// todo: MatchingCreateDto와 상속관계로 전환할 생각
@Getter
@NoArgsConstructor
public class TeamMatchingCreateRequestDto {
    @NotNull(message = "팀이 입력되지 않았습니다")
    private Long teamId;
    @NotNull(message = "이름이 입력되지 않았습니다")
    private String name;
    //todo: int는 null이면 0으로 초기화됨. 솔루션 필요함
    @NotNull(message = "경기 인원수가 입력되지 않았습니다")
    private int playPeople;
    @NotNull(message = "최대 인원수가 입력되지 않았습니다")
    private int maxPeople;
    @NotNull(message = "경기 참여 최대 팀 수가 입력되지 않았습니다")
    private int maxTeam;
    @NotNull(message = "시작 날짜/시간이 입력되지 않았습니다")
    private LocalDateTime startAt;
    @NotNull(message = "종료 날짜/시간이 입력되지 않았습니다")
    private LocalDateTime endAt;
    @NotNull(message = "지역이 입력되지 않았습니다")
    private String regionString;

    @Builder
    public TeamMatchingCreateRequestDto(
            Long teamId,
            String name,
            int playPeople,
            int maxTeam,
            int maxPeople,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String regionString
    ) {
        this.teamId = teamId;
        this.name = name;
        this.playPeople = playPeople;
        this.maxTeam = maxTeam;
        this.maxPeople = maxPeople;
        this.startAt = startAt;
        this.endAt = endAt;
        this.regionString = regionString;
    }

    public TeamMatch toEntity(Region region, Team team) {
        return TeamMatch.builder()
                .name(name)
                .playPeople(playPeople)
                .maxTeam(maxTeam)
                .maxPeople(maxPeople)
                .startAt(startAt)
                .endAt(endAt)
                .status(MatchingStatus.RECRUITING)
                .homeTeam(team)
                .region(region)
                .build();
    }
}
