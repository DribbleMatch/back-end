package com.sideProject.DribbleMatch.dto.recruitment.reqeuest;

import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RecruitmentUpdateRequestDto {
    @NotNull(message = "제목이 입력되지 않았습니다.")
    String title;
    @NotNull(message = "내용이 입력되지 않았습니다.")
    String content;
    List<Position> positions;
    int winning;

    @Builder
    public RecruitmentUpdateRequestDto(String title, String content, List<Position> positions, int winning) {
        this.title = title;
        this.content = content;
        this.positions = positions;
        this.winning = winning;
    }

}
