package com.sideProject.DribbleMatch.domain.recruitment.entity;

import com.sideProject.DribbleMatch.domain.team.entity.Team;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column
    private String content;

    @NotNull
    @Column
    private Position position;

    @NotNull
    @Column
    private int winning;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Recruitment(String title, String content, Position position, int winning, Team team) {
        this.title = title;
        this.content = content;
        this.position = position;
        this.winning = winning;
        this.team = team;
    }
}
