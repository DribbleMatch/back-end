package com.sideProject.DribbleMatch.repository.user;

import com.sideProject.DribbleMatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
}