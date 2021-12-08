package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.dto.FoodMenuDTO;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.repository.FoodMenuRepository;
import com.rokzasok.serveit.service.IDishPriceService;
import com.rokzasok.serveit.service.IFoodMenuService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodMenuService implements IFoodMenuService {
    private final FoodMenuRepository foodMenuRepository;
    private final IDishPriceService dishPriceService;

    public FoodMenuService(FoodMenuRepository foodMenuRepository,
                           IDishPriceService dishPriceService) {
        this.foodMenuRepository = foodMenuRepository;
        this.dishPriceService = dishPriceService;
    }

    @Override
    public List<FoodMenu> findAll() {
        return foodMenuRepository.findAll()
                .stream()
                .filter(table -> !table.getIsDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public FoodMenu findOne(Integer id) {
        return foodMenuRepository.findById(id).orElse(null);
    }

    @Override
    public FoodMenu save(FoodMenu entity) {
        return foodMenuRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) throws EntityNotFoundException {
        FoodMenu toDelete = findOne(id);

        if (toDelete == null)
            throw new EntityNotFoundException("Food Menu with given ID not found");

        foodMenuRepository.delete(toDelete);
        return true;
    }

    @Override
    public FoodMenu edit(Integer id, FoodMenuDTO foodMenuDTO) throws EntityNotFoundException {
        FoodMenu toEdit = findOne(id);
        if (toEdit == null)
            throw new EntityNotFoundException("Food Menu with given ID not found");
        toEdit.setDate(foodMenuDTO.getDate());

        Set<DishPrice> dishPriceSet = new HashSet<>();
        for (DishPriceDTO dto : foodMenuDTO.getDishes()){
            dishPriceSet.add(dishPriceService.findOne(dto.getId()));
        }
        toEdit.setDishes(dishPriceSet);
        return save(toEdit);
    }

    @Override
    public FoodMenu last() {
        return foodMenuRepository.findTopByOrderByDateDesc();
    }
}
