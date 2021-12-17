package com.rokzasok.serveit.controller.klimenta.integration;

import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemDTO;
import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.service.IDishOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import com.rokzasok.serveit.service.impl.DishOrderItemService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DishOrderItemControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/dish-order-items";

    @Autowired
    TestRestTemplate dispatcher;

    private static final Integer READY_DISH_ORDER_ITEM_ID = 5;
    private static final Integer CREATED_DISH_ORDER_ITEM_ID = 1;
    private static final Integer IN_PROGRESS_DISH_ORDER_ITEM_ID = 4;

    private static final Integer COOK_ID = 4;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;

    @Test
    public void testAcceptDishOrderItem_NonExistingDishOrderItem(){
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
    public void testCompleteDishOrderItem_NonExistingCook(){

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
    public void testCompleteDishOrderItem_WrongItemStatus(){

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
    public void testCompleteDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus(){

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(IN_PROGRESS_DISH_ORDER_ITEM_ID);
        dto.setWorkerId(COOK_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + IN_PROGRESS_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertNotNull(response.getBody());
    }
}
