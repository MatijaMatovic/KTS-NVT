package com.rokzasok.serveit.service.klimentaintegration;

import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.impl.DrinkOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DrinkOrderItemServiceIntegrationTest {

    @Autowired
    private DrinkOrderItemService drinkOrderItemService;

    @Autowired
    private UserService userService;

    private static final Integer DRINK_ORDER_ITEM_ID = 1;
    private static final Integer NEW_DRINK_ORDER_ITEM_ID = 2;
    private static final Integer BARTENDER_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;

    @Test
    public void changeStatusDrinkOrderItem_CompleteOrderOK() throws Exception {
        DrinkOrderItem doi = drinkOrderItemService.changeStatusDrinkOrderItem(DRINK_ORDER_ITEM_ID, ItemStatus.READY);

        assertEquals(doi.getStatus(), ItemStatus.READY);
    }

    @Test
    public void changeStatusDrinkOrderItem_NonExistingDrinkOrderItemID(){
        assertThrows(Exception.class, () -> {
            DrinkOrderItem doi = drinkOrderItemService.changeStatusDrinkOrderItem(NON_EXISTING_ID, ItemStatus.READY);;
        });
    }

    @Test
    public void acceptDrinkOrderItem_OK() throws Exception {
        DrinkOrderItem doi = drinkOrderItemService.acceptDrinkOrderItem(NEW_DRINK_ORDER_ITEM_ID,
                ItemStatus.IN_PROGRESS, BARTENDER_ID, userService);

        assertEquals(doi.getStatus(), ItemStatus.IN_PROGRESS);
    }

    @Test
    public void acceptDrinkOrderItem_NonExistingBartenderID(){

        assertThrows(Exception.class, () -> {
            DrinkOrderItem doi = drinkOrderItemService.acceptDrinkOrderItem(NEW_DRINK_ORDER_ITEM_ID,
                    ItemStatus.IN_PROGRESS, NON_EXISTING_ID, userService);
        });
    }

    @Test
    public void acceptDrinkOrderItem_NonExistingDrinkOrderItemID(){

        assertThrows(Exception.class, () -> {
            DrinkOrderItem doi = drinkOrderItemService.acceptDrinkOrderItem(NON_EXISTING_ID, ItemStatus.IN_PROGRESS,
                    BARTENDER_ID, userService);
        });
    }
}
