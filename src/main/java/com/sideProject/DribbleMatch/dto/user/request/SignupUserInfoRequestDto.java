package com.sideProject.DribbleMatch.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupUserInfoRequestDto {
    @NotNull(message = "닉네임이 입력되지 않았습니다.")
    private String nickName;
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;
    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private String birth;
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    private String password;
    @NotNull(message = "비밀번호 확인이 입력되지 않았습니다.")
    private String password_check;
    @NotNull(message = "전화번호가 입력되지 않았습니다.")
    private String phone;
    @NotNull(message = "인증번호가 입력되지 않았습니다.")
    private String authCode;

    @Builder
    public SignupUserInfoRequestDto(String nickName, String email,String birth, String password, String password_check, String phone, String authCode) {
        this.nickName = nickName;
        this.email = email;
        this.birth = birth;
        this.password = password;
        this.password_check = password_check;
        this.phone = phone;
        this.authCode = authCode;
    }
}
