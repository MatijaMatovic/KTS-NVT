package com.rokzasok.serveit.service.klimenta.unit;

import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.repository.DishOrderItemRepository;
import com.rokzasok.serveit.service.impl.DishOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
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

import static org.mockito.BDDMockito.given;

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

    private User cook;
    private Dish dish;
    private DishPrice dishPrice;
    private DishOrderItem dishOrderItem;
    private DishOrderItem newDishOrderItem;

    private static final Integer NEW_DISH_ORDER_ITEM_ID = 1;
    private static final Integer COOK_ID = 1;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 11;

    @PostConstruct
    public void setup() {
        cook = new User();
        cook.setUsername("User");
        cook.setPassword("Password");
        cook.setFirstName("Ime");
        cook.setLastName("Prezime");
        cook.setAddress("Adresa");
        cook.setImagePath("Path");
        cook.setEmail("mail@example.com");
        cook.setType(UserType.WAITER);
        cook.setPhoneNumber("123");
        cook.setIsDeleted(false);

        dish = new Dish();
        dish.setCode("code1");
        dish.setName("votka");
        dish.setCategory(DishCategory.APPETIZER);
        dish.setPreparationPrice(300.00);
        dish.setPreparationPrice(30.00);
        dish.setIsDeleted(false);
        dish.setAllergens("ds");
        dish.setIngredients("sdf");
        dish.setImagePath("sg");
        dish.setDescription("fsd");

        dishPrice = new DishPrice();
        dishPrice.setPrice(200.00);
        dishPrice.setPriceDate(LocalDate.of(2021,10,30));
        dishPrice.setIsDeleted(false);
        dishPrice.setDish(dish);

        dishOrderItem = new DishOrderItem();
        dishOrderItem.setDish(dishPrice);
        dishOrderItem.setId(DISH_ORDER_ITEM_ID);
        dishOrderItem.setCook(cook);
        dishOrderItem.setIsDeleted(false);
        dishOrderItem.setStatus(ItemStatus.IN_PROGRESS);
        dishOrderItem.setAmount(2);
        //------------------------------------------------------------------ new dish order item
        newDishOrderItem = new DishOrderItem();
        newDishOrderItem.setId(NEW_DISH_ORDER_ITEM_ID);
        newDishOrderItem.setDish(dishPrice);
        newDishOrderItem.setIsDeleted(false);
        newDishOrderItem.setStatus(ItemStatus.CREATED);
        newDishOrderItem.setAmount(2);

        List<DishOrderItem> dishOrderItems = new ArrayList<>();
        dishOrderItems.add(dishOrderItem); dishOrderItems.add(newDishOrderItem);

        given(dishOrderItemRepository.findAll()).willReturn(dishOrderItems);
        given(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).willReturn(Optional.ofNullable(dishOrderItem));
        given(dishOrderItemRepository.save(dishOrderItem)).willReturn(dishOrderItem);
        given(userService.findOne(COOK_ID)).willReturn(cook);

    }

//    @Test
//    public void changeStatusDrinkOrderItem_CompleteOrderOK() throws Exception {
//        DishOrderItem doi = dishOrderItemService.changeStatusDishOrderItem(DISH_ORDER_ITEM_ID, ItemStatus.READY);
//
//        verify(dishOrderItemRepository, times(1)).findById(DISH_ORDER_ITEM_ID);
//        verify(dishOrderItemRepository, times(1)).save(dishOrderItem);
//        assertEquals(doi, dishOrderItem);
//    }
//
//    @Test
//    public void changeStatusDrinkOrderItem_NonExistingDrinkOrderItemID() throws Exception {
//        assertThrows(Exception.class, () -> {
//            DishOrderItem doi = dishOrderItemService.changeStatusDishOrderItem(NON_EXISTING_ID, ItemStatus.READY);;
//        });
//    }
//
//    @Test
//    public void acceptDishOrderItem_OK() throws Exception {
//        DishOrderItem doi = dishOrderItemService.acceptDishOrderItem(NEW_DISH_ORDER_ITEM_ID, ItemStatus.IN_PROGRESS, COOK_ID, userService);
//
//        verify(dishOrderItemRepository, times(1)).findById(NEW_DISH_ORDER_ITEM_ID);
//        verify(userService, times(1)).findOne(COOK_ID);
//        verify(dishOrderItemRepository, times(1)).save(dishOrderItem);
//        assertEquals(doi, dishOrderItem);
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
//    public void acceptDishOrderItem_NonExistingDrinkOrderItemID(){
//
//        assertThrows(Exception.class, () -> {
//            DishOrderItem doi = dishOrderItemService.acceptDishOrderItem(NON_EXISTING_ID, ItemStatus.IN_PROGRESS, COOK_ID, userService);
//        });
//    }




}
