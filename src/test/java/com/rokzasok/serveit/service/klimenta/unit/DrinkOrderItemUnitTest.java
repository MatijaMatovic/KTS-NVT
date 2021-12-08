package com.rokzasok.serveit.service.klimenta.unit;

import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.repository.DrinkOrderItemRepository;
import com.rokzasok.serveit.service.impl.DrinkOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

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

    private User waiter;
    private Drink drink;
    private DrinkPrice drinkPrice;
    private DrinkOrderItem drinkOrderItem;
    private DrinkOrderItem newDrinkOrderItem;
    private static final Integer DRINK_ORDER_ITEM_ID = 1;
    private static final Integer NEW_DRINK_ORDER_ITEM_ID = 1;
    private static final Integer BARTENDER_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;

    @PostConstruct
    public void setup() {
        waiter = new User();
        waiter.setUsername("User");
        waiter.setPassword("Password");
        waiter.setFirstName("Ime");
        waiter.setLastName("Prezime");
        waiter.setAddress("Adresa");
        waiter.setImagePath("Path");
        waiter.setEmail("mail@example.com");
        waiter.setType(UserType.WAITER);
        waiter.setPhoneNumber("123");
        waiter.setIsDeleted(false);

        drink = new Drink();
        drink.setCode("code1");
        drink.setName("votka");
        drink.setCategory(DrinkCategory.ALCOHOLIC);
        drink.setPurchasePrice(950.00);
        drink.setIsDeleted(false);
        drink.setAllergens("ds");
        drink.setIngredients("sdf");
        drink.setImagePath("sg");
        drink.setDescription("fsd");

        drinkPrice = new DrinkPrice();
        drinkPrice.setPrice(200.00);
        drinkPrice.setPriceDate(LocalDate.of(2021, 10,30));
        drinkPrice.setIsDeleted(false);
        drinkPrice.setDrink(drink);

        drinkOrderItem = new DrinkOrderItem();
        drinkOrderItem.setDrink(drinkPrice);
        drinkOrderItem.setBartender(waiter);
        drinkOrderItem.setIsDeleted(false);
        drinkOrderItem.setStatus(ItemStatus.IN_PROGRESS);
        drinkOrderItem.setAmount(2);
        //------------------------------------------------------------------ new drink order item
        newDrinkOrderItem= new DrinkOrderItem();
        newDrinkOrderItem.setId(NEW_DRINK_ORDER_ITEM_ID);
        newDrinkOrderItem.setDrink(drinkPrice);
        newDrinkOrderItem.setIsDeleted(false);
        newDrinkOrderItem.setStatus(ItemStatus.CREATED);
        newDrinkOrderItem.setAmount(2);

        List<DrinkOrderItem> drinkOrderItems = new ArrayList<>();
        drinkOrderItems.add(drinkOrderItem);
        drinkOrderItems.add(newDrinkOrderItem);

        given(drinkOrderItemRepository.findAll()).willReturn(drinkOrderItems);
        given(drinkOrderItemRepository.findById(DRINK_ORDER_ITEM_ID)).willReturn(Optional.ofNullable(drinkOrderItem));
        given(drinkOrderItemRepository.save(drinkOrderItem)).willReturn(drinkOrderItem);
        given(userService.findOne(BARTENDER_ID)).willReturn(waiter);
    }

    @Test
    public void testFindAll() {
        List<DrinkOrderItem> foundUsers = drinkOrderItemService.findAll();
        System.out.println(foundUsers.size());
        assertEquals(2, foundUsers.size());
    }

//    @Test
//    public void changeStatusDrinkOrderItem_CompleteOrderOK() throws Exception {
//        DrinkOrderItem doi = drinkOrderItemService.changeStatusDrinkOrderItem(DRINK_ORDER_ITEM_ID, ItemStatus.READY);
//
//        verify(drinkOrderItemRepository, times(1)).findById(DRINK_ORDER_ITEM_ID);
//        verify(drinkOrderItemRepository, times(1)).save(drinkOrderItem);
//        assertEquals(doi, drinkOrderItem);
//    }
//
//    @Test
//    public void changeStatusDrinkOrderItem_NonExistingDrinkOrderItemID(){
//        assertThrows(Exception.class, () -> {
//            DrinkOrderItem doi = drinkOrderItemService.changeStatusDrinkOrderItem(NON_EXISTING_ID, ItemStatus.READY);;
//        });
//    }
//
//    @Test
//    public void acceptDrinkOrderItem_OK() throws Exception {
//        DrinkOrderItem doi = drinkOrderItemService.acceptDrinkOrderItem(NEW_DRINK_ORDER_ITEM_ID,
//                                                                        ItemStatus.IN_PROGRESS, BARTENDER_ID, userService);
//
//        verify(drinkOrderItemRepository, times(1)).findById(NEW_DRINK_ORDER_ITEM_ID);
//        verify(userService, times(1)).findOne(BARTENDER_ID);
//        verify(drinkOrderItemRepository, times(1)).save(drinkOrderItem);
//        assertEquals(doi, drinkOrderItem);
//    }
//
//    @Test
//    public void acceptDrinkOrderItem_NonExistingBartenderID(){
//
//        assertThrows(Exception.class, () -> {
//            DrinkOrderItem doi = drinkOrderItemService.acceptDrinkOrderItem(NEW_DRINK_ORDER_ITEM_ID,
//                                                                            ItemStatus.IN_PROGRESS, NON_EXISTING_ID, userService);
//        });
//    }
//
//    @Test
//    public void acceptDrinkOrderItem_NonExistingDrinkOrderItemID(){
//
//        assertThrows(Exception.class, () -> {
//            DrinkOrderItem doi = drinkOrderItemService.acceptDrinkOrderItem(NON_EXISTING_ID, ItemStatus.IN_PROGRESS,
//                                                                            BARTENDER_ID, userService);
//        });
//    }
}
