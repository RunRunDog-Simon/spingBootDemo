package com.gtalent.demo.repositories;

import com.gtalent.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    // 登入邏輯:select * from users where username = XXX;
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
