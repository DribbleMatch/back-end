package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.entity.matching.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingCustomRepository {


}
