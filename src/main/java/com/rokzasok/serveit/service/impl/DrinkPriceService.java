package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkPriceRepository;
import com.rokzasok.serveit.service.IDrinkPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class DrinkPriceService implements IDrinkPriceService {
    final
    DrinkPriceRepository drinkPriceRepository;

    public DrinkPriceService(DrinkPriceRepository drinkPriceRepository) {
        this.drinkPriceRepository = drinkPriceRepository;
    }

    @Override
    public List<DrinkPrice> findAll() {
        return drinkPriceRepository.findAll();
    }

    @Override
    public DrinkPrice findOne(Integer id) {
        return drinkPriceRepository.findById(id).orElse(null);
    }

    @Override
    public DrinkPrice save(DrinkPrice entity) {
        return drinkPriceRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) {
        DrinkPrice toDelete = findOne(id);
        if (toDelete == null)
            throw new EntityNotFoundException("Drink price with given ID not found");
        drinkPriceRepository.delete(toDelete);
        return true;
    }
}
