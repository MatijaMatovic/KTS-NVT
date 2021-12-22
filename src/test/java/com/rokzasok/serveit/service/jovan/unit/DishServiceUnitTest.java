package com.rokzasok.serveit.service.jovan.unit;

import com.rokzasok.serveit.constants.DishConstants;
import com.rokzasok.serveit.exceptions.DishNotFoundException;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.repository.DishRepository;
import com.rokzasok.serveit.service.impl.DishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DishServiceUnitTest {
    @Autowired
    private DishService dishService;

    @MockBean
    DishRepository mDishRepository;

    @Test
    public void testFindAll_ReturnsCorrectNumberOfDishes() {
        List<Dish> dishList = new ArrayList<>();
        dishList.add(new Dish());
        when(mDishRepository.findAll()).thenReturn(dishList);

        List<Dish> found = dishService.findAll();
        assertEquals(1, found.size());
    }

    @Test
    public void testFindOne_CorrectId_ReturnsDishWithCorrectId() {
        Dish dish = new Dish();
        dish.setId(DishConstants.CORRECT_ID);
        when(mDishRepository.findById(DishConstants.CORRECT_ID)).thenReturn(Optional.of(dish));

        Dish found = dishService.findOne(DishConstants.CORRECT_ID);
        assertEquals(DishConstants.CORRECT_ID, found.getId());
    }

    @Test
    public void testFindOne_WrongId_ReturnsNull() {
        when(mDishRepository.findById(DishConstants.WRONG_ID)).thenReturn(Optional.empty());
        Dish found = dishService.findOne(DishConstants.WRONG_ID);
        assertNull(found);
    }

    @Test
    public void testSave_NewId_ReturnsSavedDish() {
        when(mDishRepository.save(DishConstants.NEW_ID_DISH)).thenReturn(DishConstants.NEW_ID_DISH);

        Dish savedDish = dishService.save(DishConstants.NEW_ID_DISH);
        assertEquals(DishConstants.NEW_ID_DISH, savedDish);
    }

    @Test
    public void testSave_NoID_ReturnsSavedDishWithCorrectlyGeneratedID() {
        Dish dish = new Dish();
        dish.setId(DishConstants.NUMBER_OF_INSTANCES + 1);
        when(mDishRepository.save(DishConstants.NO_ID_DISH)).thenReturn(dish);

        Dish savedDish = dishService.save(DishConstants.NO_ID_DISH);
        assertEquals(Integer.valueOf(DishConstants.NUMBER_OF_INSTANCES + 1), savedDish.getId());
    }

    @Test
    public void testDeleteOne_CorrectId_ReturnsTrue() throws DishNotFoundException {
        Dish dish = new Dish();
        dish.setId(DishConstants.CORRECT_ID);
        when(mDishRepository.findById(DishConstants.CORRECT_ID)).thenReturn(Optional.of(dish));

        Boolean isDeletedSuccessfully = dishService.deleteOne(DishConstants.CORRECT_ID);
        assertEquals(true, isDeletedSuccessfully);
    }

    @Test(expected = DishNotFoundException.class)
    public void testDeleteOne_IncorrectId_ThrowsDishNotFoundException() throws DishNotFoundException {
        when(mDishRepository.findById(DishConstants.WRONG_ID)).thenReturn(Optional.empty());

        dishService.deleteOne(DishConstants.WRONG_ID);
    }

}
