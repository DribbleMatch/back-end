package com.sideProject.ClutchShot.dto.user.request;

import com.sideProject.ClutchShot.entity.user.ENUM.Gender;
import com.sideProject.ClutchShot.entity.user.ENUM.Skill;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupPlayerInfoRequestDto {
    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;
    @NotNull(message = "닉네임이 입력되지 않았습니다.")
    private String nickName;
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;
    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private String birth;
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    private String password;
    @NotNull(message = "전화번호가 입력되지 않았습니다.")
    private String phone;
    private int career;
    @NotNull(message = "성별이 입력되지 않았습니다.")
    private Gender gender;
    @NotNull(message = "실력이 입력되지 않았습니다.")
    private Skill skill;
    @NotNull(message = "포지션이 입력되지 않았습니다.")
    private String positionString;
    @NotNull(message = "시/도가 입력되지 않았습니다.")
    private String  siDoString;
    @NotNull(message = "시/군/구가 입력되지 않았습니다.")
    private String  siGunGuString;
    private MultipartFile image;

    @Builder
    public SignupPlayerInfoRequestDto(String name, String nickName, String email, String birth, String password, String phone, int career, Gender gender, Skill skill, String positionString, String siDoString, String siGunGuString, MultipartFile image) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.birth = birth;
        this.password = password;
        this.phone = phone;
        this.career = career;
        this.gender = gender;
        this.skill = skill;
        this.positionString = positionString;
        this.siDoString = siDoString;
        this.siGunGuString = siGunGuString;
        this.image = image;
    }
}
