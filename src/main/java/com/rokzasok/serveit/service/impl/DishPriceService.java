package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.repository.DishPriceRepository;
import com.rokzasok.serveit.service.IDishPriceService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class DishPriceService implements IDishPriceService {
    final
    DishPriceRepository dishPriceRepository;

    public DishPriceService(DishPriceRepository dishPriceRepository) {
        this.dishPriceRepository = dishPriceRepository;
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
    public Boolean deleteOne(Integer id) throws EntityNotFoundException {
        DishPrice toDelete = findOne(id);

        if (toDelete == null)
            throw new EntityNotFoundException("Dish price with given ID not found");

        dishPriceRepository.delete(toDelete);
        return true;
    }
}
