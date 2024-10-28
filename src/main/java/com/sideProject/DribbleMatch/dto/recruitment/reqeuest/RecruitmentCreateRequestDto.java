package com.sideProject.DribbleMatch.dto.recruitment.reqeuest;

import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @NotNull(message = "모집 포지션이 선택되지 않았습니다.")
    String positionString;
    @NotNull(message = "모집 종료 날짜가 선택되지 않았습니다.")
    LocalDate expireDate;

    @Builder
    public RecruitmentCreateRequestDto(Long teamId, String title, String content, String positionString, LocalDate expireDate) {
        this.teamId = teamId;
        this.title = title;
        this.content = content;
        this.positionString = positionString;
        this.expireDate = expireDate;
    }

//    public Recruitment toEntity(Team team) {
//        return Recruitment.builder()
//                .title(this.title)
//                .content(this.content)
//                .positions(this.positions)
//                .winning(this.winning)
//                .team(team)
//                .build();
//    }
}
