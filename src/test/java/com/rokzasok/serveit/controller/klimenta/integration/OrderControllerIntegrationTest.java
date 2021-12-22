package com.rokzasok.serveit.controller.klimenta.integration;

import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.dto.OrderItemDTO;
import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.model.Order;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/orders/";
    private static final Integer FINISHED_ORDER = 1;
    private static final Integer NOT_FINISHED_ORDER = 3;
    private static final Integer NOT_FINISHED_ORDER_DISH_ITEM = 7;
    private static final Integer NOT_FINISHED_ORDER_DRINK_ITEM = 5;

    @Autowired
    TestRestTemplate dispatcher;

    @Test
    public void testDeleteDishOrderItem_NonExistingOrder_ReturnStatusBadRequest(){
        //ovaj test odraditi kad se doda null
    }

    @Test
    public void testDeleteDishOrderItem_NonExistingDishOrderItem_ReturnStatusBadRequest(){
        //ovaj test odraditi kad se doda null
    }

    @Test
    public void testDeleteDishOrderItem_DishOrderItemStatusInProgress_ReturnStatusBadRequest(){
        //ovaj test odraditi kad se doda provjera statusa itema
    }

    @Test
    public void testDeleteDishOrderItem_OrderStatusFinished_ReturnStatusBadRequest(){
        //HttpEntity<OrderItemWorkerDTO> request = new HttpEntity<>(dto);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + NOT_FINISHED_ORDER + "/delete-dish-order-item/" + NOT_FINISHED_ORDER_DISH_ITEM, HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteDishOrderItem_ReturnStatusOK(){
        //odraditi nakon ispravka
    }

    @Test
    public void testDeleteDrinkOrderItem_OrderStatusFinished_ReturnStatusBadRequest(){
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + NOT_FINISHED_ORDER + "/delete-drink-order-item/" + NOT_FINISHED_ORDER_DRINK_ITEM, HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddOrderItems_OrderStatusFinished_ReturnStatusBadRequest(){
        HttpEntity<OrderItemDTO> request = new HttpEntity<>(new OrderItemDTO());
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + FINISHED_ORDER + "/add-order-items", HttpMethod.POST, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddOrderItem_OrderStatusFinished_ReturnStatusBadRequest(){
        HttpEntity<OrderItemDTO> request = new HttpEntity<>(new OrderItemDTO());
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + FINISHED_ORDER + "/add-order-item", HttpMethod.POST, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
