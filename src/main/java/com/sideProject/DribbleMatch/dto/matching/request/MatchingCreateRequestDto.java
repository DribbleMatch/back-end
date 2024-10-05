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
    @NotNull(message = "경기 인원수가 입력되지 않았습니다")
    private int playPeople;
    @NotNull(message = "경기 참여 최대 인원이 입력되지 않았습니다")
    private int maxPeople;
    @NotNull(message = "시작 날짜/시간이 입력되지 않았습니다")
    private LocalDateTime startAt;
    @NotNull(message = "경기 진행 시간이 입력되지 않았습니다")
    private int hour;
    @NotNull(message = "경기 종류가 입력되지 않았습니다")
    private GameKind gameKind;
    private String teamName;
    @NotNull(message = "여성 전용 경기 여부가 입력되지 않았습니다.")
    private GameKind isOnlyWomen;
    private String stadiumLoadAddress;
    private String stadiumJibunAddress;
    private String detailAddress;
    private String regionString;
}
