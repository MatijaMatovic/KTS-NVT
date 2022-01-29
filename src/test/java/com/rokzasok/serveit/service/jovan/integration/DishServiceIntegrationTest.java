package com.rokzasok.serveit.service.jovan.integration;

import com.rokzasok.serveit.constants.DishConstants;
import com.rokzasok.serveit.exceptions.DishNotFoundException;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.service.impl.DishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DishServiceIntegrationTest {

    @Autowired
    private DishService dishService;

    @Test
    public void testFindAll() {
        List<Dish> found = dishService.findAll();
        assertEquals(DishConstants.NUMBER_OF_INSTANCES, found.size());
    }

    @Test
    public void testFindOne_CorrectId() {
        Dish found = dishService.findOne(DishConstants.CORRECT_ID);
        assertEquals(DishConstants.CORRECT_ID, found.getId());
    }

    @Test
    public void testFindOne_WrongId() {
        Dish found = dishService.findOne(DishConstants.WRONG_ID);
        assertNull(found);
    }

    @Test
    public void testSave_NewId() {
        Dish savedDish = dishService.save(DishConstants.NEW_ID_DISH);

        assertEquals(DishConstants.NEW_ID_DISH, savedDish);
    }

    @Test
    public void testSave_ExistingId() {
        Dish savedDish = dishService.save(DishConstants.EXISTING_ID_DISH);

        assertEquals(DishConstants.EXISTING_ID_DISH, savedDish);
    }

    @Test
    public void testSave_NoId() {
        Dish savedDish = dishService.save(DishConstants.NO_ID_DISH);

        assertEquals(Integer.valueOf(DishConstants.NUMBER_OF_INSTANCES + 1), savedDish.getId());
    }


    @Test
    public void testDeleteOne_CorrectId() throws DishNotFoundException {
        Boolean isDeletedSuccessfully = dishService.deleteOne(DishConstants.CORRECT_ID);
        assertEquals(true, isDeletedSuccessfully);
    }

    @Test(expected = DishNotFoundException.class)
    public void testDeleteOne_IncorrectId() throws DishNotFoundException {
        dishService.deleteOne(DishConstants.WRONG_ID);
    }
}