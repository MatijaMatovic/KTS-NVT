package com.rokzasok.serveit.controller.klimenta.unit;

import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.repository.DishOrderItemRepository;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DishOrderItemControllerUnitTest {
    private static final String URL_PREFIX = "/api/dish-order-items";

    @Autowired
    TestRestTemplate dispatcher;

    @MockBean
    private DishOrderItemRepository dishOrderItemRepository;

    @MockBean
    private UserService userService;

    private static final Integer READY_DISH_ORDER_ITEM_ID = 5;
    private static final Integer CREATED_DISH_ORDER_ITEM_ID = 1;
    private static final Integer IN_PROGRESS_DISH_ORDER_ITEM_ID = 4;

    private static final Integer COOK_ID = 4;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;

    @Test
    public void testAcceptDishOrderItem_NonExistingDishOrderItem(){
        Mockito.when(dishOrderItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_NonExistingCook(){
        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(new DishOrderItem()));
        Mockito.when(userService.findOne(NON_EXISTING_ID)).thenReturn(null);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DISH_ORDER_ITEM_ID);
        dto.setWorkerId(NON_EXISTING_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_WrongItemStatus(){
        DishOrderItem doi = new DishOrderItem();
        doi.setStatus(ItemStatus.READY);
        User cook = new User();

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(doi));
        Mockito.when(userService.findOne(COOK_ID)).thenReturn(cook);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(READY_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus(){
        DishOrderItem dishOrderItem = new DishOrderItem();
        dishOrderItem.setId(DISH_ORDER_ITEM_ID);
        dishOrderItem.setIsDeleted(false);
        dishOrderItem.setStatus(ItemStatus.CREATED);

        User cook = new User();
        cook.setId(COOK_ID);

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(dishOrderItem));
        Mockito.when(userService.findOne(COOK_ID)).thenReturn(cook);
        Mockito.when(dishOrderItemRepository.save(dishOrderItem)).thenReturn(dishOrderItem);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(CREATED_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + CREATED_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertNotNull(response.getBody());
    }

    @Test
    public void testCompleteDishOrderItem_NonExistingDishOrderItem(){

        Mockito.when(dishOrderItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_NonExistingCook(){

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(new DishOrderItem()));
        Mockito.when(userService.findOne(NON_EXISTING_ID)).thenReturn(null);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DISH_ORDER_ITEM_ID);
        dto.setWorkerId(NON_EXISTING_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_WrongItemStatus(){
        DishOrderItem doi = new DishOrderItem();
        doi.setStatus(ItemStatus.CREATED);
        User cook = new User();

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(doi));
        Mockito.when(userService.findOne(COOK_ID)).thenReturn(cook);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(READY_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus(){

        DishOrderItem dishOrderItem = new DishOrderItem();
        dishOrderItem.setId(DISH_ORDER_ITEM_ID);
        dishOrderItem.setIsDeleted(false);
        dishOrderItem.setStatus(ItemStatus.IN_PROGRESS);

        User cook = new User();
        cook.setId(COOK_ID);

        Mockito.when(dishOrderItemRepository.findById(DISH_ORDER_ITEM_ID)).thenReturn(Optional.of(dishOrderItem));
        Mockito.when(userService.findOne(COOK_ID)).thenReturn(cook);
        Mockito.when(dishOrderItemRepository.save(dishOrderItem)).thenReturn(dishOrderItem);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(IN_PROGRESS_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + IN_PROGRESS_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertNotNull(response.getBody());
    }
}
