package com.sideProject.DribbleMatch.dto.matching.request;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingUpdateRequestDto {

    @NotNull(message = "이름이 입력되지 않았습니다")
    private String name;
    //refactor: int는 null이면 0으로 초기화됨. 솔루션 필요함
    @NotNull(message = "경기 인원수가 입력되지 않았습니다")
    private int playPeople;
    @NotNull(message = "경기 참여 최대 인원이 입력되지 않았습니다")
    private int maxPeople;
    @NotNull(message = "시작 날짜/시간이 입력되지 않았습니다")
    private LocalDateTime startAt;
    @NotNull(message = "종료 날짜/시간이 입력되지 않았습니다")
    private int hour;
    @NotNull(message = "지역이 입력되지 않았습니다")
    private String regionString;
    @NotNull(message = "경기 인원이 입력되지 않았습니다")
    private GameKind gameKind;

    @Builder
    public MatchingUpdateRequestDto(
            String name,
            int playPeople,
            int maxPeople,
            LocalDateTime startAt,
            int hour,
            String regionString,
            GameKind gameKind
    ) {
        this.name = name;
        this.playPeople = playPeople;
        this.maxPeople = maxPeople;
        this.startAt = startAt;
        this.hour = hour;
        this.regionString = regionString;
        this.gameKind = gameKind;
    }
}
