package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.exceptions.DrinkNotFoundException;
import com.rokzasok.serveit.exceptions.DrinkPriceNotFoundException;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkPriceRepository;
import com.rokzasok.serveit.repository.DrinkRepository;
import com.rokzasok.serveit.service.IDrinkPriceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrinkPriceService implements IDrinkPriceService {
    final
    DrinkPriceRepository drinkPriceRepository;
    final DrinkRepository drinkRepository;

    public DrinkPriceService(DrinkPriceRepository drinkPriceRepository, DrinkRepository drinkRepository) {
        this.drinkPriceRepository = drinkPriceRepository;
        this.drinkRepository = drinkRepository;
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
    public Boolean deleteOne(Integer id) throws Exception {
        DrinkPrice toDelete = findOne(id);
      
        if (toDelete == null)
            throw new DrinkPriceNotFoundException("Drink price with given ID not found");
      
        drinkPriceRepository.delete(toDelete);
        return true;
    }

    @Override
    public DrinkPrice edit(Integer drinkId, DrinkPriceDTO drinkPriceDTO) throws Exception {
        Drink drink = drinkRepository.findById(drinkId).orElseThrow(() -> new DrinkNotFoundException("Drink with provided ID does not exist"));
        DrinkPrice newDp = new DrinkPrice(null, drinkPriceDTO.getPrice(), drinkPriceDTO.getPriceDate(), false, drink);
        DrinkPrice savedDp = save(newDp);
        deleteOne(drinkPriceDTO.getId());
        return savedDp;
    }
}
