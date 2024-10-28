package com.sideProject.DribbleMatch.dto.recruitment.reqeuest;

import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentSearchParamRequest {
    String region;
    String sortOption;
    List<Position> positions;

    @Builder
    public RecruitmentSearchParamRequest(
            String region,
            String sortOption,
            List<Position> positions) {
        this.region = region;
        this.sortOption = sortOption;
        this.positions = positions;
    }
}
