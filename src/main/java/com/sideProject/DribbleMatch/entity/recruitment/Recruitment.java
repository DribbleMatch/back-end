package com.sideProject.DribbleMatch.entity.recruitment;

import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column
    private String content;

    @Column
    private String positionString;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column
    private LocalDate endAt;

    @Builder
    public Recruitment(String title, String content, String positionString, Team team, LocalDate endAt) {
        this.title = title;
        this.content = content;
        this.positionString = positionString;
        this.team = team;
        this.endAt = endAt;
    }
}
