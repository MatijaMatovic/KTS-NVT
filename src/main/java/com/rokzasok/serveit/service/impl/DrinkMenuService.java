package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkMenuRepository;
import com.rokzasok.serveit.service.IDrinkMenuService;
import com.rokzasok.serveit.service.IDrinkPriceService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DrinkMenuService implements IDrinkMenuService {

    private final DrinkMenuRepository drinkMenuRepository;
    private final IDrinkPriceService drinkPriceService;

    public DrinkMenuService(DrinkMenuRepository drinkMenuRepository, IDrinkPriceService drinkPriceService) {
        this.drinkMenuRepository = drinkMenuRepository;
        this.drinkPriceService = drinkPriceService;
    }

    @Override
    public List<DrinkMenu> findAll() {
        return drinkMenuRepository.findAll()
                .stream()
                .filter(table -> !table.getIsDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public DrinkMenu findOne(Integer id) {
        return drinkMenuRepository.findById(id).orElse(null);
    }

    @Override
    public DrinkMenu save(DrinkMenu entity) {
        return drinkMenuRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) throws EntityNotFoundException {
        DrinkMenu toDelete = findOne(id);

        if (toDelete == null)
            throw new EntityNotFoundException("Drink Menu with given ID not found");

        drinkMenuRepository.delete(toDelete);
        return true;
    }

    @Override
    public DrinkMenu edit(Integer id, DrinkMenuDTO drinkMenuDTO) throws EntityNotFoundException {
        DrinkMenu toEdit = findOne(id);
        if (toEdit == null)
            throw new EntityNotFoundException("Drink Menu with given ID not found");
        toEdit.setDate(drinkMenuDTO.getDate());

        Set<DrinkPrice> drinkPriceSet = new HashSet<>();
        for (DrinkPriceDTO dto : drinkMenuDTO.getDrinks()){
            drinkPriceSet.add(drinkPriceService.findOne(dto.getId()));
        }
        toEdit.setDrinks(drinkPriceSet);
        return save(toEdit);
    }

    @Override
    public DrinkMenu last() {
        return drinkMenuRepository.findTopByOrderByDateDesc();
    }
}
