package com.sideProject.DribbleMatch.repository.matching;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MatchingCustomRepository {

    public Page<Matching> searchMatchingListByStartDateOrderByStartTime(String searchWord, Pageable pageable, LocalDate date);
    public Page<Matching> findPersonalMatchingListByUserIdOrderByStartAt(Long userId, Pageable pageable, MatchingStatus status);
    public Page<Matching> findTeamMatchingListByUserIdOrderByStartAt(Long userId, Pageable pageable, MatchingStatus status);
}
