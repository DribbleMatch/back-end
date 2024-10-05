package com.sideProject.DribbleMatch.dto.matching.response;

import com.sideProject.DribbleMatch.common.util.CommonUtil;
import com.sideProject.DribbleMatch.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingUserResponseDto {
    private Long id;
    private String imagePath;
    private String nickName;
    private String positionString;
    private int level;

    @Builder
    public MatchingUserResponseDto(User user) {
        this.id = user.getId();
        this.imagePath = user.getImagePath();
        this.nickName = user.getNickName();
        this.positionString = user.getPositionString();
        this.level = CommonUtil.getLevel(user.getExperience());
    }
}
