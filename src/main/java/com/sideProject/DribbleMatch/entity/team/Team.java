package com.sideProject.DribbleMatch.entity.team;

import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    @NotNull
    private User leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @NotNull
    private Region region;

    @Builder
    public Team(String name, int winning, int maxNumber, String info, User leader, Region region) {
        this.name = name;
        this.winning = winning;
        this.maxNumber = maxNumber;
        this.info = info;
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
