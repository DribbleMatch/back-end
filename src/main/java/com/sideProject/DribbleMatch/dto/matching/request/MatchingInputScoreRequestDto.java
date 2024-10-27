package com.sideProject.DribbleMatch.dto.matching.request;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingInputScoreRequestDto {

    @NotNull(message = "매칭 ID가 입력되지 않았습니다")
    private Long id;
    @NotNull(message = "UP TEAM 점수가 입력되지 않았습니다")
    private int upTeamScore;
    @NotNull(message = "DOWN TEAM 점수가 입력되지 않았습니다")
    private int downTeamScore;
}
