package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.UserSalary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSalaryRepository extends JpaRepository<UserSalary, Integer> {
}
