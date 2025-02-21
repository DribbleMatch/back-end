package com.sideProject.ClutchShot.repository.user;

import com.sideProject.ClutchShot.entity.user.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
    Optional<User> findByEmailAndPhone(@NotNull String email, @NotNull String phone);
}