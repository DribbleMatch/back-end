package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.entity.matching.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingCustomRepository {
}
