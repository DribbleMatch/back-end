package com.sideProject.DribbleMatch.domain.team.repository;

import com.sideProject.DribbleMatch.domain.team.entity.Team;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    public Optional<Team> findByName(String name);
    public Page<Team> findAll(Pageable pageable);
}
