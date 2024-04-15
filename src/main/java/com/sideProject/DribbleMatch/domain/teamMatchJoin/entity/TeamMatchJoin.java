package com.sideProject.DribbleMatch.domain.teamMatchJoin.entity;

import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import com.sideProject.DribbleMatch.domain.team.entity.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamMatchJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Matching matching;

    @Builder
    public TeamMatchJoin(Team team, Matching matching) {
        this.team = team;
        this.matching = matching;
    }
}
