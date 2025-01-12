package com.sideProject.DribbleMatch.repository.personalMatchJoin;

import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.PersonalMatchJoin;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public interface PersonalMatchJoinCustomRepository {
    public Long countPersonalMatchJoinByMatchingAndTeam(Long matchingId, PersonalMatchingTeam matchingTeam);
    public List<User> findUserByMatchingAndTeam(Long matchingId, PersonalMatchingTeam personalMatchingTeam);
    public Optional<PersonalMatchJoin> findByMatchingIdAndUserId(Long matchingId, Long userId);
}
