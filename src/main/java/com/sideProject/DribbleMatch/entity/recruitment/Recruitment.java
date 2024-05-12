package com.sideProject.DribbleMatch.entity.recruitment;

import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<Position> positions;

    @Column
    private int winning;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Recruitment(String title, String content, List<Position> positions, int winning, Team team) {
        this.title = title;
        this.content = content;
        this.positions = positions;
        this.winning = winning;
        this.team = team;
    }
}
