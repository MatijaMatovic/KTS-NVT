package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String username);
}
