package com.rokzasok.serveit.service.klimenta.integration;

import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.IllegalUserException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.service.impl.DishOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DishOrderItemServiceIntegrationTest {
    @Autowired
    private DishOrderItemService dishOrderItemService;

    @Autowired
    private UserService userService;

    private static final Integer READY_DISH_ORDER_ITEM_ID = 5;
    private static final Integer CREATED_DISH_ORDER_ITEM_ID = 1;
    private static final Integer IN_PROGRESS_DISH_ORDER_ITEM_ID = 4;

    private static final Integer COOK_ID = 4;
    private static final Integer BARTENDER_ID = 8;
    private static final Integer ILLEGAL_COOK_ID = 9;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;

    @Test(expected = DishOrderItemNotFoundException.class)
    public void testAcceptDishOrderItem_NonExistingDishOrderItem()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        dishOrderItemService.acceptDishOrderItem(NON_EXISTING_ID, COOK_ID, userService);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAcceptDishOrderItem_NonExistingCook()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        dishOrderItemService.acceptDishOrderItem(DISH_ORDER_ITEM_ID, NON_EXISTING_ID, userService);
    }

    @Test(expected = ItemStatusSetException.class)
    public void testAcceptDishOrderItem_WrongItemStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        dishOrderItemService.acceptDishOrderItem(READY_DISH_ORDER_ITEM_ID, COOK_ID, userService);
    }

    @Test
    public void testAcceptDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DishOrderItem savedDishOrderItem = dishOrderItemService.acceptDishOrderItem(CREATED_DISH_ORDER_ITEM_ID, COOK_ID, userService);
        assertNotNull(savedDishOrderItem);
    }

    @Test(expected = DishOrderItemNotFoundException.class)
    public void testCompleteDishOrderItem_NonExistingDishOrderItem()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        dishOrderItemService.completeDishOrderItem(NON_EXISTING_ID, COOK_ID, userService);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCompleteDishOrderItem_NonExistingCook()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        dishOrderItemService.completeDishOrderItem(DISH_ORDER_ITEM_ID, NON_EXISTING_ID, userService);
    }

    @Test(expected = ItemStatusSetException.class)
    public void testCompleteDishOrderItem_WrongItemStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        dishOrderItemService.completeDishOrderItem(READY_DISH_ORDER_ITEM_ID, COOK_ID, userService);
    }

    @Test(expected = IllegalUserException.class)
    public void testCompleteDishOrderItem_CookWhoDidNotAcceptItemChangingStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException{

        DishOrderItem savedDishOrderItem = dishOrderItemService.completeDishOrderItem(IN_PROGRESS_DISH_ORDER_ITEM_ID, ILLEGAL_COOK_ID, userService);
    }

    @Test(expected = IllegalUserException.class)
    public void testCompleteDishOrderItem_BartenderChangingStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException{

        DishOrderItem savedDishOrderItem = dishOrderItemService.completeDishOrderItem(IN_PROGRESS_DISH_ORDER_ITEM_ID, BARTENDER_ID, userService);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCompleteDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        DishOrderItem savedDishOrderItem = dishOrderItemService.completeDishOrderItem(IN_PROGRESS_DISH_ORDER_ITEM_ID, COOK_ID, userService);
        assertNotNull(savedDishOrderItem);
    }
}
