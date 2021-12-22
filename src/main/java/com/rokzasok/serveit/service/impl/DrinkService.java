package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.exceptions.DrinkNotFoundException;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.repository.DrinkRepository;
import com.rokzasok.serveit.service.IDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrinkService implements IDrinkService {

    private final DrinkRepository drinkRepository;

    public DrinkService(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    @Override
    public List<Drink> findAll() {
        return drinkRepository.findAll();
    }

    @Override
    public Drink findOne(Integer id) {
        return drinkRepository.findById(id).orElse(null);
    }

    @Override
    public Drink save(Drink entity) {
        return drinkRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) throws DrinkNotFoundException {
        Drink drink = findOne(id);

        if (drink == null) {
            throw new DrinkNotFoundException("Drink not found for given id.");
        }

        drinkRepository.delete(drink);
        return true;
    }
}
