package com.sideProject.DribbleMatch.repository.stadium;

import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    public Optional<Stadium> findByName(String name);
}
