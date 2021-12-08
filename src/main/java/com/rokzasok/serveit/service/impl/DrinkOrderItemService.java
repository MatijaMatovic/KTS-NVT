package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.repository.DrinkOrderItemRepository;
import com.rokzasok.serveit.service.IDrinkOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrinkOrderItemService implements IDrinkOrderItemService {
    private final DrinkOrderItemRepository drinkOrderItemRepository;


    public DrinkOrderItemService(DrinkOrderItemRepository drinkOrderItemRepository) {
        this.drinkOrderItemRepository = drinkOrderItemRepository;
    }

    @Override
    public List<DrinkOrderItem> findAll() {
        return drinkOrderItemRepository.findAll();
    }

    @Override
    public DrinkOrderItem findOne(Integer id) {
        return drinkOrderItemRepository.findById(id).orElse(null);
    }

    @Override
    public DrinkOrderItem save(DrinkOrderItem entity) {
        return drinkOrderItemRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) {
        DrinkOrderItem drinkOrderItem = findOne(id);

        if (drinkOrderItem == null) {
            return false;
        }
        drinkOrderItemRepository.delete(drinkOrderItem);
        return true;
    }

    @Override
    public List<DrinkOrderItem> findAllByBartenderID(Integer bartenderID) {
        return drinkOrderItemRepository.findByBartenderId(bartenderID);
    }
}
