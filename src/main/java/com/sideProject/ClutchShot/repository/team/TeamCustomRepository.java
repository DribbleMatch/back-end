package com.sideProject.ClutchShot.repository.team;

import com.sideProject.ClutchShot.entity.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamCustomRepository {
    public Page<Team> findBySearch(String searchWord, Pageable pageable);
}
