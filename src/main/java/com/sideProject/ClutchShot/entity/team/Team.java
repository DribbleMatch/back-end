package com.sideProject.ClutchShot.entity.team;

import com.sideProject.ClutchShot.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.ClutchShot.entity.BaseEntity;
import com.sideProject.ClutchShot.entity.region.Region;
import com.sideProject.ClutchShot.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @Column
    private int winning;

    @Column
    private int maxNumber;

    @Column
    private String info;

    @Column
    private String tags;

    @Column
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    @NotNull
    private User leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @NotNull
    private Region region;

    @Builder
    public Team(String name, int winning, int maxNumber, String info, String tags, String imagePath, User leader, Region region) {
        this.name = name;
        this.winning = winning;
        this.maxNumber = maxNumber;
        this.info = info;
        this.tags = tags;
        this.imagePath = imagePath;
        this.leader = leader;
        this.region = region;
    }

    public void updateTeam(TeamUpdateRequestDto request, User leader, Region region) {
        this.name = request.getName();
        this.leader = leader;
        this.region = region;
    }

    public void changeLeader(User leader) {
        this.leader = leader;
    }
}
