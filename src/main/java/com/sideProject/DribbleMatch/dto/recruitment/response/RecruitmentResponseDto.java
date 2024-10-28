package com.sideProject.DribbleMatch.dto.recruitment.response;

import com.sideProject.DribbleMatch.dto.team.response.TeamDetailResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RecruitmentResponseDto {
    String title;
    Long teamId;
    String teamName;
    String teamImagePath;
    String positionString;
    LocalDate createdAt;
    LocalDate endAt;
    String content;

    @Builder
    public RecruitmentResponseDto(String title,
                                  Long teamId,
                                  String teamName,
                                  String teamImagePath,
                                  String positionString,
                                  LocalDate createdAt,
                                  LocalDate endAt,
                                  String content) {
        this.title = title;
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamImagePath = teamImagePath;
        this.positionString = positionString;
        this.createdAt = createdAt;
        this.endAt = endAt;
        this.content = content;
    }
}
