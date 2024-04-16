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

    @NotNull(message = "팀이 입력되지 않았습니다.")
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull(message = "경기가 입력되지 않았습니다.")
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Matching matching;

    @Builder
    public TeamMatchJoin(Team team, Matching matching) {
        this.team = team;
        this.matching = matching;
    }
}
