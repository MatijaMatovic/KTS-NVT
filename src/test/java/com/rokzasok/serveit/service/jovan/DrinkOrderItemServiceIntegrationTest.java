package com.rokzasok.serveit.service.jovan;

import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.service.impl.DrinkOrderItemService;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
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
public class DrinkOrderItemServiceIntegrationTest {

    private static final Integer correctId = 1;
    private static final Integer wrongId = -1;

    private static final DrinkOrderItem newIdDrinkOrderItem = new DrinkOrderItem(4, ItemStatus.CREATED, "Hitno", 1, false, null, null);
    private static final DrinkOrderItem existingIdDrinkOrderItem = new DrinkOrderItem(1, ItemStatus.CREATED, "Hitno", 1, false, null, null);
    private static final DrinkOrderItem noIdDrinkOrderItem = new DrinkOrderItem(null, ItemStatus.CREATED, "Hitno", 1, false, null, null);

    private static final int numberOfInstances = 3;
    
    private static final Integer testBartenderID = 8;
    private static final Integer wrongTestBartenderID = -1;
    private static final int numberOfItemOrdersForTestBartenderID = 3;
    
    @Autowired
    private DrinkOrderItemService drinkOrderItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private DrinkPriceService drinkPriceService;


    @Test
    public void testFindAll() {
        List<DrinkOrderItem> found = drinkOrderItemService.findAll();
        assertEquals(numberOfInstances, found.size());
    }

    @Test
    public void testFindOne_CorrectId() {
        DrinkOrderItem found = drinkOrderItemService.findOne(correctId);
        assertEquals(correctId, found.getId());
    }

    @Test
    public void testFindOne_WrongId() {
        DrinkOrderItem found = drinkOrderItemService.findOne(wrongId);
        assertNull(found);
    }

    @Test
    public void testSave_NewId_CorrectBartender_CorrectDrink() {
        User bartender = userService.findOne(1);
        DrinkPrice drinkPrice = drinkPriceService.findOne(1);

        newIdDrinkOrderItem.setBartender(bartender);
        newIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(newIdDrinkOrderItem);

        assertEquals(newIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NewId_WrongBartender_CorrectDrink() {
        User bartender = new User();
        bartender.setId(-1);
        DrinkPrice drinkPrice = drinkPriceService.findOne(1);

        newIdDrinkOrderItem.setBartender(bartender);
        newIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(newIdDrinkOrderItem);

        assertEquals(newIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NewId_CorrectBartender_WrongDrink() {
        User bartender = userService.findOne(1);
        DrinkPrice drinkPrice = new DrinkPrice();
        drinkPrice.setId(-1);

        newIdDrinkOrderItem.setBartender(bartender);
        newIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(newIdDrinkOrderItem);

        assertEquals(newIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test
    public void testSave_ExistingId_CorrectBartender_CorrectDrink() {
        User bartender = userService.findOne(1);
        DrinkPrice drinkPrice = drinkPriceService.findOne(1);

        existingIdDrinkOrderItem.setBartender(bartender);
        existingIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(existingIdDrinkOrderItem);

        assertEquals(existingIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_ExistingId_WrongBartender_CorrectDrink() {
        User bartender = new User();
        bartender.setId(-1);
        DrinkPrice drinkPrice = drinkPriceService.findOne(1);

        existingIdDrinkOrderItem.setBartender(bartender);
        existingIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(existingIdDrinkOrderItem);

        assertEquals(existingIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_ExistingId_CorrectBartender_WrongDrink() {
        User bartender = userService.findOne(1);
        DrinkPrice drinkPrice = new DrinkPrice();
        drinkPrice.setId(-1);

        existingIdDrinkOrderItem.setBartender(bartender);
        existingIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(existingIdDrinkOrderItem);

        assertEquals(existingIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test
    public void testSave_NoId_CorrectBartender_CorrectDrink() {
        User bartender = userService.findOne(1);
        DrinkPrice drinkPrice = drinkPriceService.findOne(1);

        noIdDrinkOrderItem.setBartender(bartender);
        noIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(noIdDrinkOrderItem);

        assertEquals(noIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NoId_WrongBartender_CorrectDrink() {
        User bartender = new User();
        bartender.setId(-1);
        DrinkPrice drinkPrice = drinkPriceService.findOne(1);

        noIdDrinkOrderItem.setBartender(bartender);
        noIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(noIdDrinkOrderItem);

        assertEquals(noIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NoId_CorrectBartender_WrongDrink() {
        User bartender = userService.findOne(1);
        DrinkPrice drinkPrice = new DrinkPrice();
        drinkPrice.setId(-1);

        noIdDrinkOrderItem.setBartender(bartender);
        noIdDrinkOrderItem.setDrink(drinkPrice);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(noIdDrinkOrderItem);

        assertEquals(noIdDrinkOrderItem, savedDrinkOrderItem);
    }

    @Test
    public void testDeleteOne_CorrectId() {
        Boolean isDeletedSuccessfully = drinkOrderItemService.deleteOne(correctId);
        assertEquals(true, isDeletedSuccessfully);
    }

    @Test
    public void testDeleteOne_WrongId() {
        Boolean isDeletedSuccessfully = drinkOrderItemService.deleteOne(wrongId);
        assertEquals(false, isDeletedSuccessfully);
    }

    @Test
    public void findAllByBartenderID_CorrectBartenderID() {
        List<DrinkOrderItem> allOrderItemsForBartenderID = drinkOrderItemService.findAllByBartenderID(testBartenderID);
        assertEquals(numberOfItemOrdersForTestBartenderID, allOrderItemsForBartenderID.size());
    }

    @Test
    public void findAllByBartenderID_WrongBartenderID() {
        List<DrinkOrderItem> allOrderItemsForBartenderID = drinkOrderItemService.findAllByBartenderID(wrongTestBartenderID);
        assertEquals(0, allOrderItemsForBartenderID.size());
    }
}