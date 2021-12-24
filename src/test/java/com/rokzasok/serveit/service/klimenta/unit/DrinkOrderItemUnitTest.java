package com.rokzasok.serveit.service.klimenta.unit;

import com.rokzasok.serveit.exceptions.DrinkOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.IllegalUserException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.repository.DrinkOrderItemRepository;
import com.rokzasok.serveit.service.impl.DrinkOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class DrinkOrderItemUnitTest {
    @Autowired
    private DrinkOrderItemService drinkOrderItemService;

    @MockBean
    private DrinkOrderItemRepository drinkOrderItemRepository;

    @MockBean
    private UserService userService;

    private static final Integer DRINK_ORDER_ITEM_ID = 1;
    private static final Integer BARTENDER_ID = 1;
    private static final Integer INVALID_BARTENDER_ID = 9;
    private static final Integer COOK_ID = 4;
    private static final Integer NON_EXISTING_ID = 111;

    @Test(expected = DrinkOrderItemNotFoundException.class)
    public void testAcceptDrinkOrderItem_NonExistingDrinkOrderItem()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        Mockito.when(drinkOrderItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        drinkOrderItemService.acceptDrinkOrderItem(NON_EXISTING_ID, BARTENDER_ID, userService);
    }

    @Test(expected = UserNotFoundException.class)
    public void testAcceptDrinkOrderItem_NonExistingBartender()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(new DrinkOrderItem()));
        Mockito.when(userService.findOne(NON_EXISTING_ID)).thenReturn(null);

        drinkOrderItemService.acceptDrinkOrderItem(DRINK_ORDER_ITEM_ID, NON_EXISTING_ID, userService);
    }

    @Test(expected = ItemStatusSetException.class)
    public void testAcceptDrinkOrderItem_WrongItemStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DrinkOrderItem doi = new DrinkOrderItem();
        doi.setStatus(ItemStatus.READY);
        User bartender = new User();

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(doi));
        Mockito.when(userService.findOne(BARTENDER_ID)).thenReturn(bartender);

        drinkOrderItemService.acceptDrinkOrderItem(DRINK_ORDER_ITEM_ID, BARTENDER_ID, userService);
    }

    @Test
    public void testAcceptDrinkOrderItem_CorrectDrinkOrderItem_CorrectCook_CorrectItemStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DrinkOrderItem drinkOrderItem = new DrinkOrderItem();
        drinkOrderItem.setId(DRINK_ORDER_ITEM_ID);
        drinkOrderItem.setIsDeleted(false);
        drinkOrderItem.setStatus(ItemStatus.CREATED);

        User bartender = new User();
        bartender.setId(BARTENDER_ID);

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(drinkOrderItem));
        Mockito.when(userService.findOne(BARTENDER_ID)).thenReturn(bartender);
        Mockito.when(drinkOrderItemRepository.save(drinkOrderItem)).thenReturn(drinkOrderItem);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.acceptDrinkOrderItem(DRINK_ORDER_ITEM_ID, BARTENDER_ID, userService);
        assertNotNull(savedDrinkOrderItem);
    }

    @Test(expected = DrinkOrderItemNotFoundException.class)
    public void testCompleteDrinkOrderItem_NonExistingDrinkOrderItem()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        Mockito.when(drinkOrderItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        drinkOrderItemService.completeDrinkOrderItem(NON_EXISTING_ID, BARTENDER_ID, userService);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCompleteDrinkOrderItem_NonExistingBartender()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(new DrinkOrderItem()));
        Mockito.when(userService.findOne(NON_EXISTING_ID)).thenReturn(null);

        drinkOrderItemService.completeDrinkOrderItem(DRINK_ORDER_ITEM_ID, NON_EXISTING_ID, userService);
    }

    @Test(expected = ItemStatusSetException.class)
    public void testCompleteDrinkOrderItem_WrongItemStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        DrinkOrderItem doi = new DrinkOrderItem();
        doi.setStatus(ItemStatus.CREATED);
        User bartender = new User();

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(doi));
        Mockito.when(userService.findOne(BARTENDER_ID)).thenReturn(bartender);

        drinkOrderItemService.completeDrinkOrderItem(DRINK_ORDER_ITEM_ID, BARTENDER_ID, userService);
    }

    @Test
    public void testCompleteDrinkOrderItem_CorrectDrinkOrderItem_CorrectBartender_CorrectItemStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        DrinkOrderItem drinkOrderItem = new DrinkOrderItem();
        drinkOrderItem.setId(DRINK_ORDER_ITEM_ID);
        drinkOrderItem.setIsDeleted(false);
        drinkOrderItem.setStatus(ItemStatus.IN_PROGRESS);

        User bartender = new User();
        bartender.setId(BARTENDER_ID);
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_BARTENDER"));
        bartender.setRoles(roles);

        drinkOrderItem.setBartender(bartender);

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(drinkOrderItem));
        Mockito.when(userService.findOne(BARTENDER_ID)).thenReturn(bartender);
        Mockito.when(drinkOrderItemRepository.save(drinkOrderItem)).thenReturn(drinkOrderItem);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.completeDrinkOrderItem(DRINK_ORDER_ITEM_ID, BARTENDER_ID, userService);
        assertNotNull(savedDrinkOrderItem);
    }

    @Test(expected = IllegalUserException.class)
    public void testCompleteDrinkOrderItem_BartenderWhoDidNotAcceptItemChangingStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        DrinkOrderItem drinkOrderItem = new DrinkOrderItem();
        drinkOrderItem.setId(DRINK_ORDER_ITEM_ID);
        drinkOrderItem.setIsDeleted(false);
        drinkOrderItem.setStatus(ItemStatus.IN_PROGRESS);

        User bartender = new User();
        bartender.setId(BARTENDER_ID);
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_BARTENDER"));
        bartender.setRoles(roles);

        User new_bartender = new User();
        new_bartender.setId(INVALID_BARTENDER_ID);
        new_bartender.setRoles(roles);

        drinkOrderItem.setBartender(bartender);

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(drinkOrderItem));
        Mockito.when(userService.findOne(INVALID_BARTENDER_ID)).thenReturn(new_bartender);
        Mockito.when(drinkOrderItemRepository.save(drinkOrderItem)).thenReturn(drinkOrderItem);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.completeDrinkOrderItem(DRINK_ORDER_ITEM_ID, INVALID_BARTENDER_ID, userService);
    }

    @Test(expected = IllegalUserException.class)
    public void testCompleteDrinkOrderItem_CookChangingStatus()
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException {

        DrinkOrderItem drinkOrderItem = new DrinkOrderItem();
        drinkOrderItem.setId(DRINK_ORDER_ITEM_ID);
        drinkOrderItem.setIsDeleted(false);
        drinkOrderItem.setStatus(ItemStatus.IN_PROGRESS);

        User bartender = new User();
        bartender.setId(BARTENDER_ID);
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_BARTENDER"));
        bartender.setRoles(roles);

        User cook = new User();
        cook.setId(COOK_ID);
        List<Role> new_roles = new ArrayList<>();
        new_roles.add(new Role("ROLE_COOK"));
        cook.setRoles(new_roles);

        drinkOrderItem.setBartender(bartender);

        Mockito.when(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).thenReturn(Optional.of(drinkOrderItem));
        Mockito.when(userService.findOne(COOK_ID)).thenReturn(cook);
        Mockito.when(drinkOrderItemRepository.save(drinkOrderItem)).thenReturn(drinkOrderItem);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.completeDrinkOrderItem(DRINK_ORDER_ITEM_ID, COOK_ID, userService);
    }

}
