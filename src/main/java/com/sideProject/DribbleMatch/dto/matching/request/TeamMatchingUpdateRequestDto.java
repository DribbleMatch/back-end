package com.sideProject.DribbleMatch.dto.matching.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// todo: MatchingCreateDto와 상속관계로 전환할 생각
@Getter
@NoArgsConstructor
public class TeamMatchingUpdateRequestDto {

    @NotNull(message = "이름이 입력되지 않았습니다")
    private String name;
    //todo: int는 null이면 0으로 초기화됨. 솔루션 필요함
    @NotNull(message = "경기 인원수가 입력되지 않았습니다")
    private Integer playPeople;
    @NotNull(message = "경기 참여 최대 팀 수가 입력되지 않았습니다")
    private Integer maxTeam;
    @NotNull(message = "시작 날짜/시간이 입력되지 않았습니다")
    private LocalDateTime startAt;
    @NotNull(message = "종료 날짜/시간이 입력되지 않았습니다")
    private LocalDateTime endAt;
    @NotNull(message = "지역이 입력되지 않았습니다")
    private String regionString;

    @Builder
    public TeamMatchingUpdateRequestDto(
            String name,
            int playPeople,
            int maxTeam,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String regionString
    ) {
        this.name = name;
        this.playPeople = playPeople;
        this.maxTeam = maxTeam;
        this.startAt = startAt;
        this.endAt = endAt;
        this.regionString = regionString;
    }

}
