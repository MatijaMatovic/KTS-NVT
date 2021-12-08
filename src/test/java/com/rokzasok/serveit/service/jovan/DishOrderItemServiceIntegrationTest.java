package com.rokzasok.serveit.service.jovan;

import com.rokzasok.serveit.constants.DishOrderItemConstants;
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
    @Autowired
    private DishOrderItemService dishOrderItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private DishPriceService dishPriceService;


    @Test
    public void testFindAll() {
        List<DishOrderItem> found = dishOrderItemService.findAll();
        assertEquals(DishOrderItemConstants.NUMBER_OF_INSTANCES, found.size());
    }

    @Test
    public void testFindOne_CorrectId() {
        DishOrderItem found = dishOrderItemService.findOne(DishOrderItemConstants.CORRECT_ID);
        assertEquals(DishOrderItemConstants.CORRECT_ID, found.getId());
    }

    @Test
    public void testFindOne_WrongId() {
        DishOrderItem found = dishOrderItemService.findOne(DishOrderItemConstants.WRONG_ID);
        assertNull(found);
    }

    @Test
    public void testSave_NewId_CorrectCook_CorrectDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NewId_WrongCook_CorrectDish() {
        User cook = new User();
        cook.setId(-1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NewId_CorrectCook_WrongDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = new DishPrice();
        dishPrice.setId(-1);

        DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.NEW_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test
    public void testSave_ExistingId_CorrectCook_CorrectDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_ExistingId_WrongCook_CorrectDish() {
        User cook = new User();
        cook.setId(-1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_ExistingId_CorrectCook_WrongDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = new DishPrice();
        dishPrice.setId(-1);

        DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.EXISTING_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test
    public void testSave_NoId_CorrectCook_CorrectDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NoId_WrongCook_CorrectDish() {
        User cook = new User();
        cook.setId(-1);
        DishPrice dishPrice = dishPriceService.findOne(1);

        DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void testSave_NoId_CorrectCook_WrongDish() {
        User cook = userService.findOne(1);
        DishPrice dishPrice = new DishPrice();
        dishPrice.setId(-1);

        DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM.setCook(cook);
        DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM.setDish(dishPrice);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM);

        assertEquals(DishOrderItemConstants.NO_ID_DISH_ORDER_ITEM, savedDishOrderItem);
    }

    @Test
    public void testDeleteOne_CorrectId() {
        Boolean isDeletedSuccessfully = dishOrderItemService.deleteOne(DishOrderItemConstants.CORRECT_ID);
        assertEquals(true, isDeletedSuccessfully);
    }

    @Test
    public void testDeleteOne_WrongId() {
        Boolean isDeletedSuccessfully = dishOrderItemService.deleteOne(DishOrderItemConstants.WRONG_ID);
        assertEquals(false, isDeletedSuccessfully);
    }

    @Test
    public void findAllByCookID_CorrectCookID() {
        List<DishOrderItem> allOrderItemsForCookID = dishOrderItemService.findAllByCookID(DishOrderItemConstants.TEST_COOK_ID);
        assertEquals(DishOrderItemConstants.NUMBER_OF_ITEM_ORDERS_FOR_TEST_COOK_ID, allOrderItemsForCookID.size());
    }

    @Test
    public void findAllByCookID_WrongCookID() {
        List<DishOrderItem> allOrderItemsForCookID = dishOrderItemService.findAllByCookID(DishOrderItemConstants.WRONG_TEST_COOK_ID);
        assertEquals(0, allOrderItemsForCookID.size());
    }
}