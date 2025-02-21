package com.sideProject.ClutchShot.repository.personalMatchJoin;

import com.sideProject.ClutchShot.entity.personalMatchJoin.PersonalMatchJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalMatchJoinRepository extends JpaRepository<PersonalMatchJoin, Long>, PersonalMatchJoinCustomRepository {
}
