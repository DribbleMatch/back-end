package com.sideProject.DribbleMatch.entity.joinTeam;

import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.joinTeam.ENUM.JoinStatus;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinTeam extends BaseEntity {
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
    private String introduce;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "WAIT")
    private JoinStatus status;

    public JoinTeam(User user, Team team, String introduce) {
        this.user = user;
        this.team = team;
        this.introduce = introduce;
    }
}
