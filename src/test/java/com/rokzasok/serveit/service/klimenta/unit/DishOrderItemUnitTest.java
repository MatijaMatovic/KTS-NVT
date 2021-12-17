package com.rokzasok.serveit.service.klimenta.unit;

import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.repository.DishOrderItemRepository;
import com.rokzasok.serveit.service.impl.DishOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotEquals;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class DishOrderItemUnitTest {
    @Autowired
    private DishOrderItemService dishOrderItemService;

    @MockBean
    private DishOrderItemRepository dishOrderItemRepository;

    @MockBean
    private UserService userService;

    private static final Integer COOK_ID = 1;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 11;


    @Test(expected = DishOrderItemNotFoundException.class)
    public void testAcceptDishOrderItem_NonExistingDishOrderItem()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        Mockito.when(dishOrderItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        dishOrderItemService.acceptDishOrderItem(NON_EXISTING_ID, COOK_ID, userService);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAcceptDishOrderItem_NonExistingCook()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(new DishOrderItem()));
        Mockito.when(userService.findOne(NON_EXISTING_ID)).thenReturn(null);

        dishOrderItemService.acceptDishOrderItem(DISH_ORDER_ITEM_ID, NON_EXISTING_ID, userService);
    }

    @Test(expected = ItemStatusSetException.class)
    public void testAcceptDishOrderItem_WrongItemStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DishOrderItem doi = new DishOrderItem();
        doi.setStatus(ItemStatus.READY);
        User cook = new User();

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(doi));
        Mockito.when(userService.findOne(COOK_ID)).thenReturn(cook);

        dishOrderItemService.acceptDishOrderItem(DISH_ORDER_ITEM_ID, COOK_ID, userService);
    }

    @Test
    public void testAcceptDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus()
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DishOrderItem dishOrderItem = new DishOrderItem();
        dishOrderItem.setId(DISH_ORDER_ITEM_ID);
        dishOrderItem.setIsDeleted(false);
        dishOrderItem.setStatus(ItemStatus.CREATED);

        User cook = new User();
        cook.setId(COOK_ID);

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(dishOrderItem));
        Mockito.when(userService.findOne(COOK_ID)).thenReturn(cook);
        Mockito.when(dishOrderItemRepository.save(dishOrderItem)).thenReturn(dishOrderItem);

        Optional<DishOrderItem> savedDishOrderItem = dishOrderItemService.acceptDishOrderItem(DISH_ORDER_ITEM_ID, COOK_ID, userService);
        assertNotEquals(savedDishOrderItem, Optional.empty());
    }
}
