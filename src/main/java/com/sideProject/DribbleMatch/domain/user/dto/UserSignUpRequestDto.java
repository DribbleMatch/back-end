package com.sideProject.DribbleMatch.domain.user.dto;

import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignUpRequestDto {
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    private String password;
    @NotNull(message = "비밀번호 재입력이 입력되지 않았습니다.")
    private String rePassword;
    @NotNull(message = "닉네임이 입력되지 않았습니다.")
    private String nickName;
    @NotNull(message = "성별이 입력되지 않았습니다.")
    private Gender gender;
    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private LocalDate birth;
    @NotNull(message = "포지션이 입력되지 않았습니다.")
    private Position position;

    @Builder
    public UserSignUpRequestDto(String email, String password, String rePassword, String nickName, Gender gender, LocalDate birth, Position position) {
        this.email = email;
        this.password = password;
        this.rePassword = rePassword;
        this.nickName = nickName;
        this.gender = gender;
        this.birth = birth;
        this.position = position;
    }
}
