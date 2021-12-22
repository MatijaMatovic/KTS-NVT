package com.rokzasok.serveit.service.jovan.unit;

import com.rokzasok.serveit.constants.DrinkConstants;
import com.rokzasok.serveit.exceptions.DrinkNotFoundException;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.repository.DishOrderItemRepository;
import com.rokzasok.serveit.repository.DrinkRepository;
import com.rokzasok.serveit.service.impl.DrinkService;
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
public class DrinkServiceUnitTest {
    @Autowired
    private DrinkService drinkService;

    @MockBean
    DrinkRepository mDrinkRepository;

    @Test
    public void testFindAll_ReturnsCorrectNumberOfDrinks() {
        List<Drink> drinkList = new ArrayList<>();
        drinkList.add(new Drink());
        when(mDrinkRepository.findAll()).thenReturn(drinkList);

        List<Drink> found = drinkService.findAll();
        assertEquals(1, found.size());
    }

    @Test
    public void testFindOne_CorrectId_ReturnsDrinkWithCorrectId() {
        Drink drink = new Drink();
        drink.setId(DrinkConstants.CORRECT_ID);
        when(mDrinkRepository.findById(DrinkConstants.CORRECT_ID)).thenReturn(Optional.of(drink));

        Drink found = drinkService.findOne(DrinkConstants.CORRECT_ID);
        assertEquals(DrinkConstants.CORRECT_ID, found.getId());
    }

    @Test
    public void testFindOne_WrongId_ReturnsNull() {
        when(mDrinkRepository.findById(DrinkConstants.WRONG_ID)).thenReturn(Optional.empty());
        Drink found = drinkService.findOne(DrinkConstants.WRONG_ID);
        assertNull(found);
    }

    @Test
    public void testSave_NewId_ReturnsSavedDrink() {
        when(mDrinkRepository.save(DrinkConstants.NEW_ID_DRINK)).thenReturn(DrinkConstants.NEW_ID_DRINK);

        Drink savedDrink = drinkService.save(DrinkConstants.NEW_ID_DRINK);
        assertEquals(DrinkConstants.NEW_ID_DRINK, savedDrink);
    }

    @Test
    public void testSave_NoID_ReturnsSavedDrinkWithCorrectlyGeneratedID() {
        Drink drink = new Drink();
        drink.setId(DrinkConstants.NUMBER_OF_INSTANCES + 1);
        when(mDrinkRepository.save(DrinkConstants.NO_ID_DRINK)).thenReturn(drink);

        Drink savedDrink = drinkService.save(DrinkConstants.NO_ID_DRINK);
        assertEquals(Integer.valueOf(DrinkConstants.NUMBER_OF_INSTANCES + 1), savedDrink.getId());
    }

    @Test
    public void testDeleteOne_CorrectId_ReturnsTrue() throws DrinkNotFoundException {
        Drink drink = new Drink();
        drink.setId(DrinkConstants.CORRECT_ID);
        when(mDrinkRepository.findById(DrinkConstants.CORRECT_ID)).thenReturn(Optional.of(drink));

        Boolean isDeletedSuccessfully = drinkService.deleteOne(DrinkConstants.CORRECT_ID);
        assertEquals(true, isDeletedSuccessfully);
    }

    @Test(expected = DrinkNotFoundException.class)
    public void testDeleteOne_IncorrectId_ThrowsDrinkNotFoundException() throws DrinkNotFoundException {
        when(mDrinkRepository.findById(DrinkConstants.WRONG_ID)).thenReturn(Optional.empty());

        drinkService.deleteOne(DrinkConstants.WRONG_ID);
    }

}
