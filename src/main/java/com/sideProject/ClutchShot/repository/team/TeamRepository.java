package com.sideProject.ClutchShot.repository.team;

import com.sideProject.ClutchShot.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
    public Optional<Team> findByName(String name);
}
