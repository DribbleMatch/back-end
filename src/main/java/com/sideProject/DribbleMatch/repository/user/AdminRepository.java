package com.sideProject.DribbleMatch.repository.user;

import com.sideProject.DribbleMatch.entity.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    public Optional<Admin> findByEmail(String email);
}
