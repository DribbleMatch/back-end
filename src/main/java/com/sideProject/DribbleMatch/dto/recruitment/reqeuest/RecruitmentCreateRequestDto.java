package com.sideProject.DribbleMatch.dto.recruitment.reqeuest;

import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RecruitmentCreateRequestDto {
    @NotNull(message = "팀 아이디가 입력되지 않았습니다.")
    Long teamId;
    @NotNull(message = "제목이 입력되지 않았습니다.")
    String title;
    @NotNull(message = "내용이 입력되지 않았습니다.")
    String content;
    List<Position> positions;
    int winning;

    @Builder
    public RecruitmentCreateRequestDto(Long teamId,String title, String content, List<Position> positions, int winning) {
        this.teamId = teamId;
        this.title = title;
        this.content = content;
        this.positions = positions;
        this.winning = winning;
    }

    public Recruitment toEntity(Team team) {
        return Recruitment.builder()
                .title(this.title)
                .content(this.content)
                .positions(this.positions)
                .winning(this.winning)
                .team(team)
                .build();
    }
}
