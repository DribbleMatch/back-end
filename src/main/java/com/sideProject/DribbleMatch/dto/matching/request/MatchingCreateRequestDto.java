package com.sideProject.DribbleMatch.dto.matching.request;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingCreateRequestDto {

    @NotNull(message = "이름이 입력되지 않았습니다")
    private String name;
    //todo: int는 null이면 0으로 초기화됨. 솔루션 필요함
    @NotNull(message = "경기 인원수가 입력되지 않았습니다")
    private int playPeople;
    @NotNull(message = "경기 참여 최대 인원이 입력되지 않았습니다")
    private int maxPeople;
    @NotNull(message = "시작 날짜/시간이 입력되지 않았습니다")
    private LocalDateTime startAt;
    @NotNull(message = "경기 진행 시간이 입력되지 않았습니다")
    private int hour;
    @NotNull(message = "경기장이 입력되지 않았습니다")
    private String stadiumString;
    @NotNull(message = "지역이 입력되지 않았습니다")
    private String regionString;

    public static Matching of(MatchingCreateRequestDto requestDto, Stadium stadium, Region region) {
        return Matching.builder()
                .name(requestDto.getName())
                .playPeople(requestDto.getPlayPeople())
                .maxPeople(requestDto.getMaxPeople())
                .startAt(requestDto.getStartAt())
                .hour(requestDto.hour)
                .status(MatchingStatus.RECRUITING)
                .stadium(stadium)
                .region(region)
                .gameKind(GameKind.PERSONAL)
                .imgPath(stadium == null ? "" : stadium.getImagePath())
                .build();
    }
}
