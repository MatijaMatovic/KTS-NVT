package com.rokzasok.serveit.service.matija.integration;

import com.rokzasok.serveit.model.Order;
import com.rokzasok.serveit.service.impl.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderServiceIntegrationTest {
    public static final int DB_SIZE = 2;
    public static final Integer ORDER_ID = 1;
    public static final Integer OTHER_ORDER_ID = 2;
    public static final Integer NON_EXISTENT_ORDER_ID = 420;

    @Autowired
    OrderService orderService;

    @Test
    public void testFindAll() {
        List<Order> orders = orderService.findAll();
        assertEquals(DB_SIZE, orders.size());
        for (Order o : orders) {
            assertNotNull(o.getDishes());
            assertNotNull(o.getDrinks());
        }
    }

    @Test
    public void testFindOne() {
        Order order = orderService.findOne(ORDER_ID);
        assertNotNull(order);
        assertEquals(ORDER_ID, order.getId());
    }

    @Test
    public void testFindOne_NonExistingID() {
        Order order = orderService.findOne(NON_EXISTENT_ORDER_ID);
        assertNull(order);
    }

    @Test
    public void testDeleteOne() {
        Boolean success = orderService.deleteOne(ORDER_ID);
        assertTrue(success);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteOne_NonExistingID() {
        Boolean success = orderService.deleteOne(NON_EXISTENT_ORDER_ID);
        fail("You shouldn't be here");
    }

    @Test
    public void testDeleteOne_ItemsInProgress() {
        Boolean success = orderService.deleteOne(OTHER_ORDER_ID);
        assertFalse(success);
    }
}
