package com.sideProject.DribbleMatch.dto.user.request;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @NotNull(message = "지역이 입력되지 않았습니다.")
    private String regionString;

    @Builder
    public UserSignUpRequestDto(String email, String password, String rePassword, String nickName, Gender gender, LocalDate birth, Position position, String regionString) {
        this.email = email;
        this.password = password;
        this.rePassword = rePassword;
        this.nickName = nickName;
        this.gender = gender;
        this.birth = birth;
        this.position = position;
        this.regionString = regionString;
    }

    public static User toEntity(UserSignUpRequestDto request, String password, Region region) {
        return User.builder()
                .email(request.email)
                .password(password)
                .nickName(request.nickName)
                .gender(request.gender)
                .birth(request.birth)
                .position(request.position)
                .region(region)
                .build();
    }
}
