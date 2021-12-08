package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.repository.DishOrderItemRepository;
import com.rokzasok.serveit.service.IDishOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public List<DishOrderItem> findAllByCookID(Integer cookId) {
        return dishOrderItemRepository.findByCookId(cookId);
    }
}
