package com.sideProject.DribbleMatch.dto.recruitment.reqeuest;

import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RecruitmentCreateRequestDto {
    String title;
    String content;
    List<Position> positions;
    int winning;

    @Builder
    public RecruitmentCreateRequestDto(String title, String content, List<Position> positions, int winning) {
        this.title = title;
        this.content = content;
        this.positions = positions;
        this.winning = winning;
    }

    public Recruitment toEntity() {
        return Recruitment.builder()
                .title(this.title)
                .content(this.content)
                .positions(this.positions)
                .winning(this.winning)
                .build();
    }
}
