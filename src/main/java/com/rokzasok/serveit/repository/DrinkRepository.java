package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkRepository extends JpaRepository<Drink, Integer> {
}
