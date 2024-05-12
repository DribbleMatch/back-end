package com.sideProject.DribbleMatch.repository.team;

import com.sideProject.DribbleMatch.entity.team.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}
