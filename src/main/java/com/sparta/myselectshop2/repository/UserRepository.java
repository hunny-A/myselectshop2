package com.sparta.myselectshop2.repository;

import com.sparta.myselectshop2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByKakaoId(Long id);
    Optional<User> findByEmail(String email);
}