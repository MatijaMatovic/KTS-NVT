package com.rokzasok.serveit.controller.matija.integration;

import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.model.Order;
import com.rokzasok.serveit.service.impl.OrderService;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/orders";
    private static final Integer ORDER_ID = 1;
    private static final Integer IN_PROGRESS_ORDER_ID = 2;
    private static final Integer NON_EXISTENT_ORDER_ID = 420;
    private static final Integer WAITER_ID = 6;

    @Autowired
    TestRestTemplate dispatcher;

    @Autowired
    OrderService orderService;

    @Test
    public void testGetAllOrders() {
        int dbSize = orderService.findAll().size();

        ResponseEntity<OrderDTO[]> allOrdersResponse
                = dispatcher.getForEntity(URL_PREFIX + "/all", OrderDTO[].class);

        OrderDTO[] allOrders = allOrdersResponse.getBody();

        assertNotNull(allOrders);
        assertTrue(allOrders.length > 0);
        assertEquals(dbSize, allOrders.length);
    }

    @Test
    public void testFindOneById() {
        ResponseEntity<OrderDTO> foundOrderResponse
                = dispatcher.getForEntity(URL_PREFIX + "/one/" + IN_PROGRESS_ORDER_ID, OrderDTO.class);

        OrderDTO foundOrder = foundOrderResponse.getBody();

        assertNotNull(foundOrder);
        assertEquals(IN_PROGRESS_ORDER_ID, foundOrder.getId());
    }

    @Test
    public void testFindAllByWaiter() {
        ResponseEntity<OrderDTO[]> foundOrdersResponse
                = dispatcher.getForEntity(URL_PREFIX + "/all-by-waiter/" + WAITER_ID,
                OrderDTO[].class);

        OrderDTO[] foundOrders = foundOrdersResponse.getBody();

        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.length);
        assertEquals(IN_PROGRESS_ORDER_ID, foundOrders[0].getId());
    }

    @Test
    public void testFindOneById_NonExistingID() {
        ResponseEntity<Object> foundOrderResponse
                = dispatcher.getForEntity(URL_PREFIX + "/one/" + NON_EXISTENT_ORDER_ID, Object.class);

        assertEquals(HttpStatus.NOT_FOUND, foundOrderResponse.getStatusCode());
    }

    @Test
    public void testEditOrder() {
        Order toEdit = orderService.findOne(IN_PROGRESS_ORDER_ID);
        OrderDTO toEditDTO = new OrderDTO(toEdit);

        String newNote = "This order has a new note";
        toEditDTO.setNote(newNote);

        ResponseEntity<OrderDTO> editedOrderResponse
                = dispatcher.exchange(
                        URL_PREFIX + "/edit",
                        HttpMethod.PUT,
                        new HttpEntity<>(toEditDTO),
                        OrderDTO.class
        );

        OrderDTO editedOrderDTO = editedOrderResponse.getBody();
        assertNotNull(editedOrderDTO);
        assertEquals(IN_PROGRESS_ORDER_ID, editedOrderDTO.getId());
        assertEquals(newNote, editedOrderDTO.getNote());
    }

    @Test
    public void testDeleteOrder() {
        int dbSize = orderService.findAll().size();

        ResponseEntity<Boolean> deleteSuccess
                = dispatcher.exchange(
                        URL_PREFIX + "/delete/" + ORDER_ID,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null),
                        Boolean.class
        );

        Boolean success = deleteSuccess.getBody();
        int newDbSize = orderService.findAll().size();

        assertNotNull(success);
        assertTrue(success);
        assertEquals(dbSize-1, newDbSize);
    }

    @Test
    public void testDeleteOrder_OrderInProgress() {
        int dbSize = orderService.findAll().size();

        ResponseEntity<Object> deleteSuccess
                = dispatcher.exchange(
                URL_PREFIX + "/delete/" + IN_PROGRESS_ORDER_ID,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                Object.class
        );

        int newDbSize = orderService.findAll().size();

        assertEquals(HttpStatus.BAD_REQUEST, deleteSuccess.getStatusCode());
        assertEquals(dbSize, newDbSize);
    }

    @Test
    public void testDeleteOrder_NonExistentID() {
        int dbSize = orderService.findAll().size();

        ResponseEntity<Object> deleteSuccess
                = dispatcher.exchange(
                URL_PREFIX + "/delete/" + NON_EXISTENT_ORDER_ID,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                Object.class
        );

        int newDbSize = orderService.findAll().size();

        assertEquals(HttpStatus.NOT_FOUND, deleteSuccess.getStatusCode());
        assertEquals(dbSize, newDbSize);
    }
}
