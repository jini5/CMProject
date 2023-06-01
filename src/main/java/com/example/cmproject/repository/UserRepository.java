package com.example.cmproject.repository;


import com.example.cmproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Boolean existsByEmail(String email);

    User deleteByEmail(String email);

    Optional<User> findByNickName(String nickName);
}
