package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
