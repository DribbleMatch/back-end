package com.sideProject.DribbleMatch.entity.matching;

import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import com.sideProject.DribbleMatch.entity.team.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamMatch extends Matching{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @ColumnDefault("2")
    private int maxTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    @NotNull
    private Team homeTeam;

    @Builder
    public TeamMatch(
            String name,
            int playPeople,
            int maxPeople,
            LocalDateTime startAt,
            LocalDateTime endAt,
            MatchingStatus status,
            Region region,
            Stadium stadium,
            Team homeTeam,
            int maxTeam) {
        super(name, playPeople, maxPeople, startAt, endAt, status, region, stadium);
        this.homeTeam = homeTeam;
        this.maxTeam = maxTeam;
    }

    public void update(TeamMatchingUpdateRequestDto request, Region region) {
        super.update(
                request.getName(),
                request.getPlayPeople(),
                request.getMaxTeam(),
                request.getStartAt(),
                request.getEndAt(),
                region
        );
        this.maxTeam = request.getMaxTeam();
    }

}
