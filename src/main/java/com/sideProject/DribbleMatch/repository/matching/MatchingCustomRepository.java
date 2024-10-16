package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.entity.matching.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MatchingCustomRepository {

    public Page<Matching> searchMatchingsByStartDateOrderByStartTime(String searchWord, Pageable pageable, LocalDate date);
}
