package com.sideProject.DribbleMatch.dto.user.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDto {

    private String imagePath;
    private String nickName;
    private String positionString;
    private String ageAndGender;
    private String skillString;
    private String regionString;
    private int level;
    private double experience;

    @Builder
    public UserResponseDto(String imagePath,
                           String nickName,
                           String positionString,
                           String ageAndGender,
                           String skillString,
                           String regionString,
                           int level,
                           double experience) {
        this.imagePath = imagePath;
        this.nickName = nickName;
        this.positionString = positionString;
        this.ageAndGender = ageAndGender;
        this.skillString = skillString;
        this.regionString = regionString;
        this.level = level;
        this.experience = experience;
    }
}
