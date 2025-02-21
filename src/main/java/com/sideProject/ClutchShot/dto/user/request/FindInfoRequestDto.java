package com.sideProject.ClutchShot.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindInfoRequestDto {
    String name;
    private String email;
    private String birth;
    @NotNull(message = "전화번호가 입력되지 않았습니다.")
    private String phoneNum;

    @Builder
    public FindInfoRequestDto(String name,
                              String email,
                              String birth,
                              String phoneNum) {
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.phoneNum = phoneNum;
    }
}
