package com.sideProject.ClutchShot.entity.teamMatchJoin;

import com.sideProject.ClutchShot.entity.BaseEntity;
import com.sideProject.ClutchShot.entity.matching.Matching;
import com.sideProject.ClutchShot.entity.teamMember.TeamMember;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamMatchJoin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_member_id")
    private TeamMember teamMember;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Matching matching;

    @Builder
    public TeamMatchJoin(TeamMember teamMember, Matching matching) {
        this.teamMember = teamMember;
        this.matching = matching;
    }
}
