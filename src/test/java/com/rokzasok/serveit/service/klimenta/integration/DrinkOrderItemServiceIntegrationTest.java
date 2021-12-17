package com.rokzasok.serveit.service.klimenta.integration;

import com.rokzasok.serveit.exceptions.DrinkOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.service.impl.DrinkOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DrinkOrderItemServiceIntegrationTest {

    @Autowired
    private DrinkOrderItemService drinkOrderItemService;

    @Autowired
    private UserService userService;

    private static final Integer DRINK_ORDER_ITEM_ID = 1;
    private static final Integer READY_DRINK_ORDER_ITEM_ID = 2;
    private static final Integer BARTENDER_ID = 8;
    private static final Integer NON_EXISTING_ID = 111;

    @Test(expected = DrinkOrderItemNotFoundException.class)
    public void testAcceptDrinkOrderItem_NonExistingDrinkOrderItem()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        drinkOrderItemService.acceptDrinkOrderItem(NON_EXISTING_ID, BARTENDER_ID, userService);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAcceptDrinkOrderItem_NonExistingBartender()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        drinkOrderItemService.acceptDrinkOrderItem(DRINK_ORDER_ITEM_ID, NON_EXISTING_ID, userService);
    }

    @Test(expected = ItemStatusSetException.class)
    public void testAcceptDrinkOrderItem_WrongItemStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        drinkOrderItemService.acceptDrinkOrderItem(READY_DRINK_ORDER_ITEM_ID, BARTENDER_ID, userService);
    }

    @Test
    public void testAcceptDrinkOrderItem_CorrectDrinkOrderItem_CorrectCook_CorrectItemStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.acceptDrinkOrderItem(DRINK_ORDER_ITEM_ID, BARTENDER_ID, userService);
        assertNotNull(savedDrinkOrderItem);
    }



}
