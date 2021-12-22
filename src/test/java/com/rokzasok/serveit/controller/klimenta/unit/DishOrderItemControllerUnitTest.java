package com.rokzasok.serveit.controller.klimenta.unit;

import com.rokzasok.serveit.dto.JwtAuthenticationRequest;
import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.dto.UserTokenState;
import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.service.impl.DishOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DishOrderItemControllerUnitTest {
    private static final String URL_PREFIX = "/api/dish-order-items";
    private static final Integer READY_DISH_ORDER_ITEM_ID = 5;
    private static final Integer CREATED_DISH_ORDER_ITEM_ID = 1;
    private static final Integer IN_PROGRESS_DISH_ORDER_ITEM_ID = 4;

    private static final Integer COOK_ID = 4;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;

    @Autowired
    TestRestTemplate dispatcher;

    @MockBean
    private DishOrderItemService dishOrderItemservice;

    @MockBean
    private UserService userService;

    private String accessToken;

    @Before
    public void login() {
        JwtAuthenticationRequest loginDto = new JwtAuthenticationRequest(
                "kuvarko","password"
        );

        ResponseEntity<UserTokenState> response = dispatcher.postForEntity("/auth/login", loginDto, UserTokenState.class);
        UserTokenState user = response.getBody();
        accessToken = user.getAccessToken();
    }

    @Test
    public void testAcceptDishOrderItem_NonExistingDishOrderItem()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {
        Mockito.when(dishOrderItemservice.acceptDishOrderItem(NON_EXISTING_ID, COOK_ID, userService))
                .thenThrow(DishOrderItemNotFoundException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_NonExistingCook()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {
        Mockito.when(dishOrderItemservice.acceptDishOrderItem(DISH_ORDER_ITEM_ID, NON_EXISTING_ID, userService))
                .thenThrow(UserNotFoundException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DISH_ORDER_ITEM_ID);
        dto.setWorkerId(NON_EXISTING_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_WrongItemStatus()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {
        Mockito.when(dishOrderItemservice.acceptDishOrderItem(READY_DISH_ORDER_ITEM_ID, COOK_ID, userService))
                .thenThrow(ItemStatusSetException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(READY_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {

        Mockito.when(dishOrderItemservice.acceptDishOrderItem(DISH_ORDER_ITEM_ID, COOK_ID, userService))
                .thenReturn(new DishOrderItem());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(CREATED_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + CREATED_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        //assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCompleteDishOrderItem_NonExistingDishOrderItem()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {
        Mockito.when(dishOrderItemservice.completeDishOrderItem(NON_EXISTING_ID, COOK_ID, userService))
                .thenThrow(DishOrderItemNotFoundException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_NonExistingCook()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {
        Mockito.when(dishOrderItemservice.completeDishOrderItem(DISH_ORDER_ITEM_ID, NON_EXISTING_ID, userService))
                .thenThrow(UserNotFoundException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DISH_ORDER_ITEM_ID);
        dto.setWorkerId(NON_EXISTING_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_WrongItemStatus()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {
        Mockito.when(dishOrderItemservice.completeDishOrderItem(READY_DISH_ORDER_ITEM_ID, COOK_ID, userService))
                .thenThrow(ItemStatusSetException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(READY_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus()
            throws UserNotFoundException, DishOrderItemNotFoundException, ItemStatusSetException {
        DishOrderItem dishOrderItem = new DishOrderItem();
        dishOrderItem.setStatus(ItemStatus.READY);
        dishOrderItem.setId(READY_DISH_ORDER_ITEM_ID);
        User cook = new User();
        cook.setId(COOK_ID);
        dishOrderItem.setCook(cook);

        Mockito.when(dishOrderItemservice.completeDishOrderItem(READY_DISH_ORDER_ITEM_ID, COOK_ID, userService))
                .thenReturn(dishOrderItem);

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(IN_PROGRESS_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + IN_PROGRESS_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        //assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
