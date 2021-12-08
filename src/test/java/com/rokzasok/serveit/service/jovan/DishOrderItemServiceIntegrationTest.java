package com.rokzasok.serveit.service.jovan;

import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.service.impl.DishOrderItemService;
import com.rokzasok.serveit.service.impl.DishPriceService;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DishOrderItemServiceIntegrationTest {

    private static final Integer correctId = 1;
    private static final Integer wrongId = -1;

    private static final DishOrderItem newIdDishOrderItem = new DishOrderItem(7, ItemStatus.CREATED, "Pozuriti", 1, 1, false, null, null);
    private static final DishOrderItem existingIdDishOrderItem = new DishOrderItem(1, ItemStatus.CREATED, "Pozuriti", 1, 1, false, null, null);
    private static final DishOrderItem noIdDishOrderItem = new DishOrderItem(null, ItemStatus.CREATED, "Pozuriti", 1, 1, false, null, null);


    private static final int numberOfInstances = 6;

    private static final Integer testCookID = 3;
    private static final Integer wrongTestCookID = -1;
    private static final int numberOfItemOrdersForTestCookID = 3;

    @Autowired
    private DishOrderItemService dishOrderItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private DishPriceService dishPriceService;


    @Test
    public void testFindAll() {
        List<DishOrderItem> found = dishOrderItemService.findAll();
        assertEquals(numberOfInstances, found.size());
    }

    @Test
    public void testFindOne_CorrectId() {
        DishOrderItem found = dishOrderItemService.findOne(correctId);
        assertEquals(correctId, found.getId());
    }

    @Test
    public void testFindOne_WrongId() {
        DishOrderItem found = dishOrderItemService.findOne(wrongId);
        assertNull(found);
    }

    @Test
    public void testSave_NewId_CorrectCook_CorrectDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        newIdDishOrderItem.setCook(cook);
        newIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(newIdDishOrderItem);

        assertEquals(newIdDishOrderItem, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NewId_WrongCook_CorrectDish() {
        User cook = new User();
        cook.setId(-1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        newIdDishOrderItem.setCook(cook);
        newIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(newIdDishOrderItem);

        assertEquals(newIdDishOrderItem, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NewId_CorrectCook_WrongDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = new DishPrice();
        dishPrice.setId(-1);

        newIdDishOrderItem.setCook(cook);
        newIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(newIdDishOrderItem);

        assertEquals(newIdDishOrderItem, savedDishOrderItem);
    }

    @Test
    public void testSave_ExistingId_CorrectCook_CorrectDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        existingIdDishOrderItem.setCook(cook);
        existingIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(existingIdDishOrderItem);

        assertEquals(existingIdDishOrderItem, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_ExistingId_WrongCook_CorrectDish() {
        User cook = new User();
        cook.setId(-1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        existingIdDishOrderItem.setCook(cook);
        existingIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(existingIdDishOrderItem);

        assertEquals(existingIdDishOrderItem, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_ExistingId_CorrectCook_WrongDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = new DishPrice();
        dishPrice.setId(-1);

        existingIdDishOrderItem.setCook(cook);
        existingIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(existingIdDishOrderItem);

        assertEquals(existingIdDishOrderItem, savedDishOrderItem);
    }

    @Test
    public void testSave_NoId_CorrectCook_CorrectDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        noIdDishOrderItem.setCook(cook);
        noIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(noIdDishOrderItem);

        assertEquals(noIdDishOrderItem, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NoId_WrongCook_CorrectDish() {
        User cook = new User();
        cook.setId(-1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        noIdDishOrderItem.setCook(cook);
        noIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(noIdDishOrderItem);

        assertEquals(noIdDishOrderItem, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NoId_CorrectCook_WrongDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = new DishPrice();
        dishPrice.setId(-1);

        noIdDishOrderItem.setCook(cook);
        noIdDishOrderItem.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(noIdDishOrderItem);

        assertEquals(noIdDishOrderItem, savedDishOrderItem);
    }

    @Test
    public void testDeleteOne_CorrectId() {
        Boolean isDeletedSuccessfully = dishOrderItemService.deleteOne(correctId);
        assertEquals(true, isDeletedSuccessfully);
    }

    @Test
    public void testDeleteOne_WrongId() {
        Boolean isDeletedSuccessfully = dishOrderItemService.deleteOne(wrongId);
        assertEquals(false, isDeletedSuccessfully);
    }

    @Test
    public void findAllByCookID_CorrectCookID() {
        List<DishOrderItem> allOrderItemsForCookID = dishOrderItemService.findAllByCookID(testCookID);
        assertEquals(numberOfItemOrdersForTestCookID, allOrderItemsForCookID.size());
    }

    @Test
    public void findAllByCookID_WrongCookID() {
        List<DishOrderItem> allOrderItemsForCookID = dishOrderItemService.findAllByCookID(wrongTestCookID);
        assertEquals(0, allOrderItemsForCookID.size());
    }
}