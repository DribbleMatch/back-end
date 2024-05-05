package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.entity.matching.PersonalMatching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalMatchingRepository extends JpaRepository<PersonalMatching, Long> {
}
