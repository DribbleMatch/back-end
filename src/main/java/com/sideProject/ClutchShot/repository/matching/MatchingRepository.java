package com.sideProject.ClutchShot.repository.matching;

import com.sideProject.ClutchShot.entity.matching.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingCustomRepository {
}
