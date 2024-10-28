package com.sideProject.DribbleMatch.dto.matching.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecentMatchingResponseDto {

    private Long id;
    private String remainTime;
    private String matchingName;

    @Builder
    public RecentMatchingResponseDto(Long id,
                                     String remainTime,
                                     String matchingName) {
        this.id = id;
        this.remainTime = remainTime;
        this.matchingName = matchingName;
    }
}
