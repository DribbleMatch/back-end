package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.IsReservedStadium;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingDetailTestResponseDto extends MatchingSimpleResponseDto{

    private int upTeamMemberNum;
    private int downTeamMemberNum;
    private int upTeamScore;
    private int downTeamScore;
}
