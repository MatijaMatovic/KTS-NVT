package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.exceptions.DishNotFoundException;
import com.rokzasok.serveit.exceptions.DishPriceNotFoundException;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.repository.DishPriceRepository;
import com.rokzasok.serveit.repository.DishRepository;
import com.rokzasok.serveit.service.IDishPriceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishPriceService implements IDishPriceService {
    final
    DishPriceRepository dishPriceRepository;
    final
    DishRepository dishRepository;

    public DishPriceService(DishPriceRepository dishPriceRepository, DishRepository dishRepository) {
        this.dishPriceRepository = dishPriceRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public List<DishPrice> findAll() {
        return dishPriceRepository.findAll();
    }

    @Override
    public DishPrice findOne(Integer id) {
        return dishPriceRepository.findById(id).orElse(null);
    }

    @Override
    public DishPrice save(DishPrice entity) {
        return dishPriceRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) throws Exception {
        DishPrice toDelete = findOne(id);

        if (toDelete == null)
            throw new DishPriceNotFoundException("Dish price with given ID not found");

        dishPriceRepository.delete(toDelete);
        return true;
    }

    @Override
    public DishPrice edit(Integer dishId, DishPriceDTO dishPriceDTO) throws Exception {
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new DishNotFoundException("Dish with provided ID does not exist"));
        DishPrice newDp = new DishPrice(null, dishPriceDTO.getPrice(), dishPriceDTO.getPriceDate(), false, dish);
        DishPrice savedDp = save(newDp);
        deleteOne(dishPriceDTO.getId());
        return savedDp;
    }
}
