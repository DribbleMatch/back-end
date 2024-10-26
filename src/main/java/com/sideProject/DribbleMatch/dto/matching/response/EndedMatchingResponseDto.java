package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EndedMatchingResponseDto {

    private Long id;
    private String scoreString;
    private String winTeamName;
    private String upTeamName;
    private String downTeamName;
    private String startAt;
    private String time;
    private String regionString;
    private String playMemberNum;

    @Builder
    public EndedMatchingResponseDto(Long id,
                                    String scoreString,
                                    List<String> teamNameList,
                                    String startAt,
                                    String time,
                                    String regionString,
                                    String playMemberNum) {
        this.id = id;
        this.scoreString = scoreString;
        this.startAt = startAt;
        this.time = time;
        this.regionString = regionString;
        this.playMemberNum = playMemberNum;

        if (teamNameList.size() == 2) {
            this.winTeamName = teamNameList.get(1);
            this.upTeamName = teamNameList.get(0);
            this.downTeamName = teamNameList.get(1);
        } else if (teamNameList.size() == 1) {
            this.winTeamName = "(팀 미정)";
            this.upTeamName = teamNameList.get(0);
            this.downTeamName = "(팀 미정)";
        } else {
            this.winTeamName = "A팀";
            this.upTeamName = "A팀";
            this.downTeamName = "B팀";
        }
    }
}
