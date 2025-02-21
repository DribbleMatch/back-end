package com.sideProject.ClutchShot.dto.recruitment.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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
