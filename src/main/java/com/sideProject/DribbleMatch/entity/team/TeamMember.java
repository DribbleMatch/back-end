package com.sideProject.DribbleMatch.entity.team;

import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
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
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull(message = "사용자가 입력되지 않았습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "팀이 입력되지 않았습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
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

    public void advancement() {
        this.teamRole = TeamRole.ADMIN;
    }
}
