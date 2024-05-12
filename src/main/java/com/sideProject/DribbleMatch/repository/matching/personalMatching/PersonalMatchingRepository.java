package com.sideProject.DribbleMatch.repository.matching.personalMatching;

import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
import com.sideProject.DribbleMatch.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalMatchingRepository extends JpaRepository<PersonalMatching, Long>, PersonalMatchingCustomRepository {
    public Optional<PersonalMatching> findByName(String name);
}
