package com.learn.SecurityBackend.repo;

import com.learn.SecurityBackend.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WelcomeRepo extends JpaRepository<Login,Integer> {
    Optional<Login> findByUsername(String name);
}
