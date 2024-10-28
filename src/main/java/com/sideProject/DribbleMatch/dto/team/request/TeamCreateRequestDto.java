package com.sideProject.DribbleMatch.dto.team.request;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCreateRequestDto {
    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;

    @NotNull(message = "최대 팀 인원이 입력되지 않았습니다.")
    private int maxNum;

    private String info;

    @NotNull(message = "지역이 입력되지 않았습니다.")
    private String regionString;

    private MultipartFile image;

    private String tags;

    @Builder
    public TeamCreateRequestDto(String name, int maxNum, String info, String regionString, MultipartFile image, String tags) {
        this.name = name;
        this.maxNum = maxNum;
        this.info = info;
        this.regionString = regionString;
        this.image = image;
        this.tags = tags;
    }
}
