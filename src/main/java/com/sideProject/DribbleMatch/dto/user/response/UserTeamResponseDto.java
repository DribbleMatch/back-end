package com.sideProject.DribbleMatch.dto.user.response;

import com.sideProject.DribbleMatch.common.util.CommonUtil;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeamResponseDto {

    private String imagePath;
    private String nickName;
    private Gender gender;
    private int level;
    private double experiencePercent;

    @Builder
    public UserTeamResponseDto(String imagePath, String nickName, Gender gender, int level, double experiencePercent) {
        this.imagePath = imagePath;
        this.nickName = nickName;
        this.gender = gender;
        this.level = level;
        this.experiencePercent = experiencePercent;
    }

    public static UserTeamResponseDto of(User user) {
        return UserTeamResponseDto.builder()
                .imagePath(user.getImagePath())
                .nickName(user.getNickName())
                .gender(user.getGender())
                .level(CommonUtil.getLevel(user.getExperience()))
                .experiencePercent(CommonUtil.getExperiencePercentToLevelUp(user.getExperience()))
                .build();
    }
}
