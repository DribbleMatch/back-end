package com.sideProject.DribbleMatch.entity.teamMatchJoin;

import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.team.Team;
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
    private TeamMatch teamMatching;


    @Builder
    public TeamMatchJoin(Team team, TeamMatch teamMatching) {
        this.team = team;
        this.teamMatching = teamMatching;
    }
}
