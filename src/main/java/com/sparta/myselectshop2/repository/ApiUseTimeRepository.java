package com.sparta.myselectshop2.repository;

import com.sparta.myselectshop2.entity.ApiUseTime;
import com.sparta.myselectshop2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
    Optional<ApiUseTime> findByUser(User user);
}