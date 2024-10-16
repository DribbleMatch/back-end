package com.sideProject.DribbleMatch.repository.team;

import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.entity.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamCustomRepository {
    public Page<Team> findBySearch(String searchWord, Pageable pageable);
}
