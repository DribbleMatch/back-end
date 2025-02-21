package com.sideProject.ClutchShot.entity.teamMember;

import com.sideProject.ClutchShot.entity.BaseEntity;
import com.sideProject.ClutchShot.entity.teamMember.ENUM.TeamRole;
import com.sideProject.ClutchShot.entity.team.Team;
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
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull(message = "사용자가 입력되지 않았습니다.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "팀이 입력되지 않았습니다.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column
    @Enumerated(EnumType.STRING)
    private TeamRole teamRole;

    @Builder
    public TeamMember(User user, Team team, TeamRole teamRole) {
        this.user = user;
        this.team = team;
        this.teamRole = teamRole;
    }

    public void changeTeamRole(TeamRole teamRole) {
        this.teamRole = teamRole;
    }
}
