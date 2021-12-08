package com.rokzasok.serveit.service.klimentaintegration;

import com.rokzasok.serveit.service.impl.DishOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DishOrderItemServiceIntegrationTest {
    @Autowired
    private DishOrderItemService dishOrderItemService;

    @Autowired
    private UserService userService;

    private static final Integer NEW_DISH_ORDER_ITEM_ID = 2;
    private static final Integer COOK_ID = 1;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;

//    @Test
//    public void changeStatusDishOrderItem_CompleteOrderOK() throws Exception{
//        DishOrderItem doi = dishOrderItemService.changeStatusDishOrderItem(DISH_ORDER_ITEM_ID, ItemStatus.READY);
//        assert doi.getStatus() == ItemStatus.READY;
//    }
//
//    @Test
//    public void changeStatusDrinkOrderItem_NonExistingDishOrderItemID() throws Exception {
//        assertThrows(Exception.class, () -> {
//            DishOrderItem doi = dishOrderItemService.changeStatusDishOrderItem(NON_EXISTING_ID, ItemStatus.READY);;
//        });
//    }
//
//    @Test
//    public void acceptDishOrderItem_OK() throws Exception {
//        DishOrderItem doi = dishOrderItemService.acceptDishOrderItem(NEW_DISH_ORDER_ITEM_ID, ItemStatus.IN_PROGRESS, COOK_ID, userService);
//
//        assertEquals(doi.getStatus(), ItemStatus.IN_PROGRESS);
//        assertEquals(doi.getCook().getId(), COOK_ID);
//    }
//
//    @Test
//    public void acceptDishOrderItem_NonExistingBartenderID(){
//
//        assertThrows(Exception.class, () -> {
//            DishOrderItem doi = dishOrderItemService.acceptDishOrderItem(NEW_DISH_ORDER_ITEM_ID, ItemStatus.IN_PROGRESS, NON_EXISTING_ID, userService);
//        });
//    }
//
//    @Test
//    public void acceptDishOrderItem_NonExistingDishOrderItemID(){
//
//        assertThrows(Exception.class, () -> {
//            DishOrderItem doi = dishOrderItemService.acceptDishOrderItem(NON_EXISTING_ID, ItemStatus.IN_PROGRESS, COOK_ID, userService);
//        });
//    }
}
