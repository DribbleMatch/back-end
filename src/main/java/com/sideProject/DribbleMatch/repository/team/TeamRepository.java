package com.sideProject.DribbleMatch.repository.team;

import com.sideProject.DribbleMatch.entity.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
    public Optional<Team> findByName(String name);
}
