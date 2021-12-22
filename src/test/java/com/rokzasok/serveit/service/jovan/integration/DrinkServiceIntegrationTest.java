package com.rokzasok.serveit.service.jovan.integration;

import com.rokzasok.serveit.constants.DrinkConstants;
import com.rokzasok.serveit.exceptions.DrinkNotFoundException;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.service.impl.DrinkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DrinkServiceIntegrationTest {
    @Autowired
    private DrinkService drinkService;

    @Test
    public void testFindAll_ReturnsCorrectNumberOfDrinks() {
        List<Drink> found = drinkService.findAll();
        assertEquals(DrinkConstants.NUMBER_OF_INSTANCES, found.size());
    }

    @Test
    public void testFindOne_CorrectId_ReturnsDrinkWithCorrectId() {
        Drink found = drinkService.findOne(DrinkConstants.CORRECT_ID);
        assertEquals(DrinkConstants.CORRECT_ID, found.getId());
    }

    @Test
    public void testFindOne_WrongId_ReturnsNull() {
        Drink found = drinkService.findOne(DrinkConstants.WRONG_ID);
        assertNull(found);
    }

    @Test
    public void testSave_NewId() {
        Drink savedDrink = drinkService.save(DrinkConstants.NEW_ID_DRINK);

        assertEquals(DrinkConstants.NEW_ID_DRINK, savedDrink);
    }

    @Test
    public void testSave_NewId_ReturnsSavedDrink() {
        Drink savedDrink = drinkService.save(DrinkConstants.EXISTING_ID_DRINK);

        assertEquals(DrinkConstants.EXISTING_ID_DRINK, savedDrink);
    }

    @Test
    public void testSave_NoID_ReturnsSavedDrinkWithCorrectlyGeneratedID() {
        Drink savedDrink = drinkService.save(DrinkConstants.NO_ID_DRINK);

        assertEquals(Integer.valueOf(DrinkConstants.NUMBER_OF_INSTANCES + 1), savedDrink.getId());
    }

    @Test
    public void testDeleteOne_CorrectId_ReturnsTrue() throws DrinkNotFoundException {
        Boolean isDeletedSuccessfully = drinkService.deleteOne(DrinkConstants.CORRECT_ID);
        assertEquals(true, isDeletedSuccessfully);
    }

    @Test(expected = DrinkNotFoundException.class)
    public void testDeleteOne_IncorrectId() throws DrinkNotFoundException {
        drinkService.deleteOne(DrinkConstants.WRONG_ID);
    }
}