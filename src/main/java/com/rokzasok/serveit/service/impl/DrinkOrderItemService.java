package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.repository.DrinkOrderItemRepository;
import com.rokzasok.serveit.service.IDrinkOrderItemService;
import com.rokzasok.serveit.service.IUserService;
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
    public DrinkOrderItem changeStatusDrinkOrderItem(Integer id, ItemStatus itemStatus) throws Exception {
        DrinkOrderItem drinkOrderItem = findOne(id);

        if (drinkOrderItem == null)
            throw new Exception("Drink order item with given id doesn't exist");

        drinkOrderItem.setStatus(itemStatus);
        return drinkOrderItemRepository.save(drinkOrderItem);
    }

    @Override
    public DrinkOrderItem acceptDrinkOrderItem(Integer id, ItemStatus itemStatus, Integer bartenderId, IUserService userService) throws Exception {
        DrinkOrderItem drinkOrderItem = findOne(id);
        if (drinkOrderItem == null)
            throw new Exception("Drink order item with given id doesn't exist");

        User user = userService.findOne(bartenderId);
        if (user == null)
            throw new Exception("Bartender with given id doesn't exist");

        drinkOrderItem.setStatus(itemStatus);
        drinkOrderItem.setBartender(user);

        return drinkOrderItemRepository.save(drinkOrderItem);
    }

    @Override
    public List<DrinkOrderItem> findAllByBartenderID(Integer bartenderID) {
        return drinkOrderItemRepository.findByBartenderId(bartenderID);
    }
}
