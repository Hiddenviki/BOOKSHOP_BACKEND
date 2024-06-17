package com.pet.Bookshop.repository;

import com.pet.Bookshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByLogin(String username);
    boolean existsByEmailOrLogin(String email, String login);
}
