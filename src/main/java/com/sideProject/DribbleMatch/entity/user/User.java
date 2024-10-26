package com.sideProject.DribbleMatch.entity.user;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column
    @NotNull
    private String password;

    @Column(unique = true)
    @NotNull
    private String nickName;

    @Column
    @NotNull
    private Gender gender;

    @Column
    @NotNull
    private LocalDate birth;

    @Column
    @NotNull
    private String positionString;

    @Column
    private int winning;

    @ManyToOne
    @JoinColumn(name = "region_id")
    @NotNull
    private Region region;

    @Column
    private int experience;

    @Column
    private String imagePath;

    //todo: 숙련도 및 농구 경력 처리
//     경험치
//    0 ~ 100: 1레벨
//    100 ~ 300: 2레벨
//    300 ~ 600: 3레벨

    @Builder
    public User(String email, String password, String nickName, Gender gender, LocalDate birth, String positionString, int winning, Region region, int experience, String imagePath) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.birth = birth;
        this.positionString = positionString;
        this.winning = winning;
        this.region = region;
        this.experience = experience;
        this.imagePath = imagePath;
    }
}
