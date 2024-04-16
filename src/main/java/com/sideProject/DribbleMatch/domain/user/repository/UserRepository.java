package com.sideProject.DribbleMatch.domain.user.repository;

import com.sideProject.DribbleMatch.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
