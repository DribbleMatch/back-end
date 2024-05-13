package com.sideProject.DribbleMatch.dto.recruitment.reqeuest;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitmentSearchParamRequest {
    String region;
    String sortOption;

    @Builder
    public RecruitmentSearchParamRequest(String region, String sortOption) {
        this.region = region;
        this.sortOption = sortOption;
    }
}
