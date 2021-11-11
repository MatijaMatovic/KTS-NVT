package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.service.IDrinkService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrinkService implements IDrinkService {
    @Override
    public List<Drink> findAll() {
        return null;
    }

    @Override
    public Drink findOne(Integer id) {
        return null;
    }

    @Override
    public Drink save(Drink entity) {
        return null;
    }

    @Override
    public Boolean deleteOne(Integer id) {
        return null;
    }
}
