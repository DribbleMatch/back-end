package com.sideProject.DribbleMatch.entity.recruitment;

import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.position.PositionListConverter;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Recruitment extends BaseEntity {

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
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Position> positions;

    @Column
    private int winning;

    @Column
    private int views;

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
        this.views = 0;
    }

    public void update(String title, String content, List<Position> positions, int winning) {
        this.title = title;
        this.content = content;
        this.positions = positions;
        this.winning = winning;
    }

    public void read() {
        this.views += 1;
    }
}
