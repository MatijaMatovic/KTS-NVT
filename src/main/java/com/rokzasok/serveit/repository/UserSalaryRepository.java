package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSalaryRepository extends JpaRepository<UserSalary, Integer> {
    public List<UserSalary> findByUser(User user);
}
