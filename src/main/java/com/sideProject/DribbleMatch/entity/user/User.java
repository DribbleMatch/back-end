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
    private Position position;

    @Column
    private int winning;

    @ManyToOne
    @JoinColumn(name = "region_id")
    @NotNull
    private Region region;

    @Builder
    public User(String email, String password, String nickName, Gender gender, LocalDate birth, Position position, int winning, Region region) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.birth = birth;
        this.position = position;
        this.winning = winning;
        this.region = region;
    }
}
