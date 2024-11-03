package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NotInputScoreMatchingResponseDto {

    private Long id;
    private String upTeamName;
    private String downTeamName;
    private String startAt;
    private String regionString;

    @Builder
    public NotInputScoreMatchingResponseDto(Long id,
                                            List<String> teamNameList,
                                            String startAt,
                                            String regionString) {
        this.id = id;
        this.startAt = startAt;
        this.regionString = regionString;

        if (teamNameList.size() == 2) {
            this.upTeamName = teamNameList.get(0);
            this.downTeamName = teamNameList.get(1);
        } else if (teamNameList.size() == 1) {
            this.upTeamName = teamNameList.get(0);
            this.downTeamName = "모집중";
        } else if (teamNameList.isEmpty()) {
            this.upTeamName = "A팀";
            this.downTeamName = "B팀";
        } else {
            throw new CustomException(ErrorCode.INVALID_TEAM_MATCH_INFO);
        }
    }
}
