package com.sideProject.DribbleMatch.domain.team.entity;

import com.sideProject.DribbleMatch.domain.team.dto.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.domain.team.dto.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.domain.user.entity.User;
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
    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;

    @Column
    @NotNull(message = "지역이 입력되지 않았습니다.")
    private String region;

    @Column
    private int winning;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    @NotNull(message = "팀장이 입력되지 않았습니다.")
    private User leader;

    @Builder
    public Team(String name, String region, int winning, User leader) {
        this.name = name;
        this.region = region;
        this.winning = winning;
        this.leader = leader;
    }

    public void updateTeam(TeamUpdateRequestDto request, User leader) {
        this.name = request.getName();
        this.region = request.getRegion();
        this.leader = leader;
    }
}
