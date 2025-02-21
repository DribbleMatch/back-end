package com.sideProject.ClutchShot.dto.matching.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchingUserDetailResponseDto extends MatchingDetailTestResponseDto{

    private List<MatchingUserResponseDto> upTeamMember;
    private List<MatchingUserResponseDto> downTeamMember;
}
