package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodMenuRepository extends JpaRepository<FoodMenu, Integer> {
}
