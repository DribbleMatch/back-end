package com.sideProject.ClutchShot.dto.matching.request;

import com.sideProject.ClutchShot.entity.matching.ENUM.GameKind;
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
    private int playNum;
    @NotNull(message = "경기 참여 최대 인원이 입력되지 않았습니다")
    private int maxNum;
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
