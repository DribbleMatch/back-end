package com.sideProject.DribbleMatch.entity.matching;

import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PersonalMatching extends Matching{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Builder
    public PersonalMatching(
            String name,
            int playPeople,
            int maxPeople,
            LocalDateTime startAt,
            LocalDateTime endAt,
            MatchingStatus status,
            Region region,
            Stadium stadium) {
        super(name, playPeople, maxPeople, startAt, endAt, status, region, stadium);
    }
}
