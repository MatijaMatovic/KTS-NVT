package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.repository.DishOrderItemRepository;
import com.rokzasok.serveit.service.IDishOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishOrderItemService implements IDishOrderItemService {
    private final DishOrderItemRepository dishOrderItemRepository;

    public DishOrderItemService(DishOrderItemRepository dishOrderItemRepository) {
        this.dishOrderItemRepository = dishOrderItemRepository;
    }

    @Override
    public List<DishOrderItem> findAll() {
        return dishOrderItemRepository.findAll();
    }

    @Override
    public DishOrderItem findOne(Integer id) {
        return dishOrderItemRepository.findById(id).orElse(null);
    }

    @Override
    public DishOrderItem save(DishOrderItem entity) {
        return dishOrderItemRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) {
        DishOrderItem dishOrderItem = findOne(id);

        if (dishOrderItem == null) {
            return false;
        }
        dishOrderItemRepository.delete(dishOrderItem);
        return true;
    }

    @Override
    public List<DishOrderItem> findAllByCookID(Integer cookId) {
        return dishOrderItemRepository.findByCookId(cookId);
    }

    @Override
    public DishOrderItem acceptDishOrderItem(Integer id, Integer cookId, IUserService userService)
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {
        DishOrderItem dishOrderItem = dishOrderItemRepository.findById(id).orElseThrow(
                                                    ()-> new DishOrderItemNotFoundException("DishOrderItem Not Found"));

        User cook = userService.findOne(cookId);
        if (cook == null)
            throw new UserNotFoundException("Cook Not Found");

        if(dishOrderItem.getStatus() != ItemStatus.CREATED)
            throw new ItemStatusSetException("Can not change item status from" + dishOrderItem.getStatus().name() + "to IN_PROGRESS");

        dishOrderItem.setCook(cook);
        dishOrderItem.setStatus(ItemStatus.IN_PROGRESS);
        DishOrderItem savedDishOrderItem = save(dishOrderItem);
        return savedDishOrderItem;
    }

    @Override
    public DishOrderItem completeDishOrderItem(Integer id, Integer cookId, IUserService userService)
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {
        DishOrderItem dishOrderItem = dishOrderItemRepository.findById(id).orElseThrow(()-> new DishOrderItemNotFoundException("DishOrderItem Not Found"));

        User cook = userService.findOne(cookId);
        if (cook == null)
            throw new UserNotFoundException("Cook Not Found");

        if(dishOrderItem.getStatus() != ItemStatus.IN_PROGRESS)
            throw new ItemStatusSetException("Can not change item status from" + dishOrderItem.getStatus().name() + "to READY");

        dishOrderItem.setStatus(ItemStatus.READY);
        return save(dishOrderItem);
    }

    @Override
    public DishOrderItem deliverDishOrderItem(Integer id)
            throws DishOrderItemNotFoundException, ItemStatusSetException {
        DishOrderItem dishOrderItem = dishOrderItemRepository
                .findById(id)
                .orElseThrow(
                        () -> new DishOrderItemNotFoundException("DishOrderItem with given ID not found")
                );

        if (dishOrderItem.getStatus() != ItemStatus.READY)
            throw new ItemStatusSetException("Cannot change item status from " + dishOrderItem.getStatus().name() + " to DELIVERED");

        dishOrderItem.setStatus(ItemStatus.DELIVERED);
        return save(dishOrderItem);
    }

}
