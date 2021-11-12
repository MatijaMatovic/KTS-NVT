package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.repository.DishRepository;
import com.rokzasok.serveit.service.IDishService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService implements IDishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    @Override
    public Dish findOne(Integer id) {
        return dishRepository.findById(id).orElse(null);
    }

    @Override
    public Dish save(Dish entity) {
        return dishRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) {
        Dish dish = findOne(id);

        if (dish == null) {
            return false;
        }

        dishRepository.delete(dish);
        return true;
    }
}
