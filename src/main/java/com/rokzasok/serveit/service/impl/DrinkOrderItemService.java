package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.exceptions.DrinkOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.repository.DrinkOrderItemRepository;
import com.rokzasok.serveit.service.IDrinkOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    @Override
    public DrinkOrderItem completeDrinkOrderItem(Integer id, Integer workerId, IUserService userService)
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {
        DrinkOrderItem drinkOrderItem = drinkOrderItemRepository.findById(id)
                                        .orElseThrow(()-> new DrinkOrderItemNotFoundException("DrinkOrderItem Not Found"));

        User bartender = userService.findOne(workerId);
        if (bartender == null )
            throw new UserNotFoundException("Bartender Not Found");

        if(drinkOrderItem.getStatus() != ItemStatus.IN_PROGRESS)
            throw new ItemStatusSetException("Can not change item status from" + drinkOrderItem.getStatus().name() + "to READY");

        drinkOrderItem.setStatus(ItemStatus.READY);
        return save(drinkOrderItem);
    }

    public DrinkOrderItem acceptDrinkOrderItem(Integer id, Integer workerId, IUserService userService)
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {
        DrinkOrderItem drinkOrderItem = drinkOrderItemRepository.findById(id)
                .orElseThrow(()-> new DrinkOrderItemNotFoundException("DrinkOrderItem Not Found"));

        User bartender = userService.findOne(workerId);
        if (bartender == null)
            throw new UserNotFoundException("Bartender Not Found");

        if(drinkOrderItem.getStatus() != ItemStatus.CREATED)
            throw new ItemStatusSetException("Can not change item status from" + drinkOrderItem.getStatus().name() + "to IN_PROGRESS");

        drinkOrderItem.setBartender(bartender);
        drinkOrderItem.setStatus(ItemStatus.IN_PROGRESS);
        return save(drinkOrderItem);
    }
}
