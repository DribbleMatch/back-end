package com.sideProject.DribbleMatch.domain.userTeam.entity;

import com.sideProject.DribbleMatch.domain.team.entity.Team;
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
public class UserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull(message = "사용자가 입력되지 않았습니다.")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "팀이 입력되지 않았습니다.")
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public UserTeam(User user, Team team) {
        this.user = user;
        this.team = team;
    }
}
