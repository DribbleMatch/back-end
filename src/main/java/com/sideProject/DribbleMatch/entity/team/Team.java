package com.sideProject.DribbleMatch.entity.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.User;
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

    @Column(unique = true)
    @NotNull
    private String name;

    @Column
    private int winning;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    @NotNull
    private User leader;

    @ManyToOne
    @JoinColumn(name = "region_id")
    @NotNull
    private Region region;

    @Builder
    public Team(String name, int winning, User leader, Region region) {
        this.name = name;
        this.winning = winning;
        this.leader = leader;
        this.region = region;
    }

    public void updateTeam(String name, User leader, Region region) {
        this.name = name;
        this.leader = leader;
        this.region = region;
    }
}
