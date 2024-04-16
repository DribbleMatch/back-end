package com.sideProject.DribbleMatch.domain.matching.repository;

import com.sideProject.DribbleMatch.domain.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
}
