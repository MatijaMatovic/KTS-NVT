package com.rokzasok.serveit.controller.klimenta.integration;

import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.exceptions.DrinkOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DrinkOrderItem;
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
public class DrinkOrderItemControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/dish-order-items";

    @Autowired
    TestRestTemplate dispatcher;
    private static final Integer DRINK_ORDER_ITEM_ID = 1;
    private static final Integer READY_DRINK_ORDER_ITEM_ID = 2;
    private static final Integer IN_PROGRESS_DRINK_ORDER_ITEM_ID = 4;
    private static final Integer BARTENDER_ID = 8;
    private static final Integer NON_EXISTING_ID = 111;

    @Test
    public void testAcceptDrinkOrderItem_NonExistingDrinkOrderItem(){
        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setWorkerId(BARTENDER_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDrinkOrderItem_NonExistingBartender(){
        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DRINK_ORDER_ITEM_ID);
        dto.setWorkerId(NON_EXISTING_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDrinkOrderItem_WrongItemStatus(){
        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(READY_DRINK_ORDER_ITEM_ID);
        dto.setWorkerId(BARTENDER_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + READY_DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAcceptDrinkOrderItem_CorrectDrinkOrderItem_CorrectCook_CorrectItemStatus(){

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DRINK_ORDER_ITEM_ID);
        dto.setWorkerId(BARTENDER_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertNotNull(response.getBody());
    }

    @Test
    public void testCompleteDrinkOrderItem_NonExistingDrinkOrderItem(){

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(NON_EXISTING_ID);
        dto.setWorkerId(BARTENDER_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDrinkOrderItem_NonExistingBartender(){

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DRINK_ORDER_ITEM_ID);
        dto.setWorkerId(NON_EXISTING_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDrinkOrderItem_WrongItemStatus(){

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(DRINK_ORDER_ITEM_ID);
        dto.setWorkerId(BARTENDER_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCompleteDrinkOrderItem_CorrectDrinkOrderItem_CorrectBartender_CorrectItemStatus(){

        OrderItemWorkerDTO dto = new OrderItemWorkerDTO();
        dto.setId(IN_PROGRESS_DRINK_ORDER_ITEM_ID);
        dto.setWorkerId(BARTENDER_ID);
        HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + IN_PROGRESS_DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertNotNull(response.getBody());

    }

}

