package com.sideProject.DribbleMatch.domain.matching.entity;

import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.domain.matching.entity.ENUM.MatchingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull(message = "이름이 입력되지 않았습니다.")
    @Column
    private String name;

    @Column
    private int people;

    @NotNull(message = "시작시간이 입력되지 않았습니다.")
    @Column
    LocalDateTime startAt;

    @NotNull(message = "종료시간이 입력되지 않았습니다.")
    @Column
    LocalDateTime endAt;

    @NotNull(message = "장소가 입력되지 않았습니다.")
    @Column
    String place;

    @NotNull(message = "상태가 입력되지 않았습니다.")
    @Column
    MatchingStatus status;

    @NotNull(message = "타입이 입력되지 않았습니다.")
    @Column
    MatchingType type;

    @Builder
    public Matching(String name, int people, LocalDateTime startAt, LocalDateTime endAt, String place, MatchingStatus status, MatchingType type) {
        this.name = name;
        this.people = people;
        this.startAt = startAt;
        this.endAt = endAt;
        this.place = place;
        this.status = status;
        this.type = type;
    }
}
