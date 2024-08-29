package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.entity.matching.Matching;

import java.time.LocalDate;
import java.util.List;

public interface MatchingCustomRepository {
    public List<Matching> findByStartDateOrderByStartTime(LocalDate localDate);
}
