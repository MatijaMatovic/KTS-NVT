package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.repository.DishOrderItemRepository;
import com.rokzasok.serveit.service.IDishOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Boolean changeStatusDishOrderItem(Integer id, ItemStatus itemStatus) {
        DishOrderItem dishOrderItem = findOne(id);

        if (dishOrderItem == null)
            return false;

        dishOrderItemRepository.changeStatusDishOrderItem(id, itemStatus.name());
        return true;
    }

    @Override
    public Boolean acceptDishOrderItem(Integer id, ItemStatus itemStatus, Integer cookId) {
        DishOrderItem dishOrderItem = findOne(id);

        if (dishOrderItem == null)
            return false;

        dishOrderItemRepository.acceptDishOrderItem(id, itemStatus.name(), cookId);
        return true;
    }
  
    @Override
    public List<DishOrderItem> findAllByOrderID(Integer orderID) {
        return null;
    }

    @Override
    public List<DishOrderItem> findAllByCookID(Integer cookId) {
        return dishOrderItemRepository.findByCookId(cookId);
    }
}
