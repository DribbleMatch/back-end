package com.sideProject.DribbleMatch.repository.teamMember;

import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberCustomRepository {
    Optional<TeamMember> findByUserIdAndTeamId(Long userId, Long teamId);
    List<TeamMember> findByTeamId(Long teamId);
    Page<TeamMember> findByUserId(Long userId, Pageable pageable);
}
