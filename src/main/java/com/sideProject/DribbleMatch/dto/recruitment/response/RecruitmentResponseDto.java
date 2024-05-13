package com.sideProject.DribbleMatch.dto.recruitment.response;

import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RecruitmentResponseDto {
    Long id;
    String title;
    String content;
    List<Position> positions;
    int winning;
    int views;
    TeamResponseDto team;
    LocalDateTime createAt;

    @Builder
    public RecruitmentResponseDto(Long id,
                                  String title,
                                  String content,
                                  List<Position> positions,
                                  int winning,
                                  int views,
                                  TeamResponseDto team,
                                  LocalDateTime createAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.positions = positions;
        this.winning = winning;
        this.views = views;
        this.team = team;
        this.createAt = createAt;
    }

    public static RecruitmentResponseDto of(Recruitment recruitment, Team team){
        return RecruitmentResponseDto.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .content(recruitment.getContent())
                .positions(recruitment.getPositions())
                .winning(recruitment.getWinning())
                .views(recruitment.getViews())
                .createAt(recruitment.getCreatedAt())
                .team(TeamResponseDto.of(team, team.getRegion().getSiDo()))
                .build();
    }
}
