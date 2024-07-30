package com.pet.Bookshop.repository;

import com.pet.Bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String username);
    boolean existsByEmailOrLogin(String email, String login);
}
