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
public class ReservedMatchingResponseDto {

    private Long id;
    private String upTeamName;
    private String downTeamName;
    private int upTeamMemberNum;
    private int downTeamMemberNum;
    private String startAt;
    private String time;
    private String regionString;
    private String playMemberNum;
    private int maxMemberNum;

    @Builder
    public ReservedMatchingResponseDto(Long id,
                                       LinkedHashMap<String, List<TeamMember>> teamInfo,
                                       LinkedHashMap<String, List<User>> userInfo,
                                       String startAt,
                                       String time,
                                       String regionString,
                                       String playMemberNum,
                                       int maxMemberNum) {
        this.id = id;
        this.startAt = startAt;
        this.time = time;
        this.regionString = regionString;
        this.playMemberNum = playMemberNum;
        this.maxMemberNum = maxMemberNum;

        List<String> teamNameList = new ArrayList<>();
        List<String> userTeamNameList = new ArrayList<>();

        if (teamInfo != null) {
            teamNameList = new ArrayList<>(teamInfo.keySet());
        } else {
            userTeamNameList = new ArrayList<>(userInfo.keySet());
        }

        if (teamNameList.size() == 2) {
            this.upTeamName = teamNameList.get(0);
            this.downTeamName = teamNameList.get(1);
            this.upTeamMemberNum = teamInfo.get(teamNameList.get(0)).size();
            this.downTeamMemberNum = teamInfo.get(teamNameList.get(1)).size();
        } else if (teamNameList.size() == 1) {
            this.upTeamName = teamNameList.get(0);
            this.downTeamName = "(팀 미정)";
            this.upTeamMemberNum = teamInfo.get(teamNameList.get(0)).size();
            this.downTeamMemberNum = 0;
        } else if (userTeamNameList.size() == 2) {
            this.upTeamName = userTeamNameList.get(0);
            this.downTeamName = userTeamNameList.get(1);
            this.upTeamMemberNum = userInfo.get(userTeamNameList.get(0)).size();
            this.downTeamMemberNum = userInfo.get(userTeamNameList.get(1)).size();
        } else {
            throw new CustomException(ErrorCode.INVALID_TEAM_MATCH_INFO);
        }
    }
}
