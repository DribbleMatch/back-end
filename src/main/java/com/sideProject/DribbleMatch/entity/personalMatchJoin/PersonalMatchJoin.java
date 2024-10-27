package com.sideProject.DribbleMatch.entity.personalMatchJoin;

import com.sideProject.DribbleMatch.entity.BaseEntity;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
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
public class PersonalMatchJoin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private PersonalMatchingTeam matchingTeam;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Matching matching;

    @Builder
    public PersonalMatchJoin(PersonalMatchingTeam matchingTeam, User user, Matching matching) {
        this.matchingTeam = matchingTeam;
        this.user = user;
        this.matching = matching;
    }
}
