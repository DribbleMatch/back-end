package com.sideProject.ClutchShot.dto.matching.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingDetailTestResponseDto extends MatchingSimpleResponseDto{

    private int upTeamMemberNum;
    private int downTeamMemberNum;
    private int upTeamScore;
    private int downTeamScore;
}
