package com.sideProject.DribbleMatch.dto.user.request;

import jakarta.persistence.MapKeyColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignInRequest {
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    @Builder
    public UserSignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
