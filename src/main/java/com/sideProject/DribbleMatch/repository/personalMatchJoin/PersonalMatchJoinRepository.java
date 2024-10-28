package com.sideProject.DribbleMatch.repository.personalMatchJoin;

import com.sideProject.DribbleMatch.entity.personalMatchJoin.PersonalMatchJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalMatchJoinRepository extends JpaRepository<PersonalMatchJoin, Long>, PersonalMatchJoinCustomRepository {
}
