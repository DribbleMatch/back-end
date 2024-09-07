package com.sideProject.DribbleMatch.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLogInRequestDto {
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    @Builder
    public UserLogInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
