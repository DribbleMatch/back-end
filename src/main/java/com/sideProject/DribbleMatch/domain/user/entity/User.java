package com.sideProject.DribbleMatch.domain.user.entity;

import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String email;

    @Column
    @NotNull(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    @Column(unique = true)
    @NotNull(message = "닉네임이 입력되지 않았습니다.")
    private String nickName;

    @Column
    @NotNull(message = "성별이 입력되지 않았습니다.")
    private Gender gender;

    @Column
    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private LocalDate birth;

    @Column
    @NotNull(message = "포지션이 입력되지 않았습니다.")
    private Position position;

    @Column
    private int winning;

    @Builder
    public User(String email, String password, String nickName, Gender gender, LocalDate birth, Position position, int winning) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.birth = birth;
        this.position = position;
        this.winning = winning;
    }
}
