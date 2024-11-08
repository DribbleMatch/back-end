package com.sideProject.DribbleMatch.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordRequestDto {
    @NotNull(message = "사용자가 입력되지 않았습니다.")
    private Long userId;
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    private String password;
    @NotNull(message = "비밀번호 확인이 입력되지 않았습니다.")
    private String rePassword;

    @Builder
    public ChangePasswordRequestDto(Long userId,
                                    String password,
                                    String rePassword) {
        this.userId = userId;
        this.password = password;
        this.rePassword = rePassword;
    }
}
