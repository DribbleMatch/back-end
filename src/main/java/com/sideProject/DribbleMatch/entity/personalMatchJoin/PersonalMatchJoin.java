//package com.sideProject.DribbleMatch.entity.personalMatchJoin;
//
//import com.sideProject.DribbleMatch.entity.matching.Matching;
//import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
//import com.sideProject.DribbleMatch.entity.user.User;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//public class PersonalMatchJoin {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "personal_match_id")
//    private PersonalMatching personalMatching;
//
//    @Builder
//    public PersonalMatchJoin(User user, PersonalMatching personalMatching) {
//        this.user = user;
//        this.personalMatching = personalMatching;
//    }
//}
