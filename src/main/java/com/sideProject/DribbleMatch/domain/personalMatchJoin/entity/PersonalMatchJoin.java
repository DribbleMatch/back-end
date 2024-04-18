package com.sideProject.DribbleMatch.domain.personalMatchJoin.entity;

import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
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
public class PersonalMatchJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "사용자가 입력되지 않았습니다.")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "경기가 입력되지 않았습니다.")
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Matching matching;

    @Builder
    public PersonalMatchJoin(User user, Matching matching) {
        this.user = user;
        this.matching = matching;
    }
}
