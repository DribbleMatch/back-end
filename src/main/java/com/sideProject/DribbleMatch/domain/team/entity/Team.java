package com.sideProject.DribbleMatch.domain.team.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;

    @Column
    @NotNull(message = "지역이 입력되지 않았습니다.")
    private String region;

    @Column
    private int winning;

    @Builder
    public Team(String name, String region, int winning) {
        this.name = name;
        this.region = region;
        this.winning = winning;
    }
}
