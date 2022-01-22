package com.rokzasok.serveit.service.matija.unit;

import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.repository.OrderRepository;
import com.rokzasok.serveit.service.impl.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderServiceUnitTest {
    public static final Integer ORDER_ID = 1;
    public static final Integer OTHER_ORDER_ID = 55;
    public static final Integer NON_EXISTENT_ORDER_ID = 420;

    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    Order order1, order2;
    DishOrderItem doi1, doi2, doi3, doi4;
    DishPrice dp1, dp2, dp3, dp4;
    Dish d1, d2, d3, d4;

    @PostConstruct
    public void setup() {

        // Initialize dummy classes
        {
            d1 = new Dish();
            d1.setCode("d1");
            d2 = new Dish();
            d2.setCode("d2");
            d3 = new Dish();
            d3.setCode("d3");
            d4 = new Dish();
            d4.setCode("d3");


            dp1 = new DishPrice();
            dp1.setDish(d1);
            dp1.setPrice(100.);

            dp2 = new DishPrice();
            dp2.setDish(d2);
            dp2.setPrice(100.);

            dp3 = new DishPrice();
            dp3.setDish(d3);
            dp3.setPrice(100.);

            dp4 = new DishPrice();
            dp4.setDish(d4);
            dp4.setPrice(100.);

            doi1 = new DishOrderItem();
            doi1.setId(1);
            doi1.setStatus(ItemStatus.CREATED);

            doi2 = new DishOrderItem();
            doi2.setId(2);
            doi2.setStatus(ItemStatus.CREATED);

            doi3 = new DishOrderItem();
            doi3.setId(3);
            doi3.setStatus(ItemStatus.CREATED);

            doi4 = new DishOrderItem();
            doi4.setId(4);
            doi4.setStatus(ItemStatus.IN_PROGRESS);

        }

        // Create sets of data
        Set<DishOrderItem> dishes1 = Set.of(doi1, doi2);
        Set<DishOrderItem> dishes2 = Set.of(doi3, doi4);

        // Create orders
        order1 = new Order();
        order1.setId(ORDER_ID);
        order1.setDishes(dishes1);

        order2 = new Order();
        order2.setId(OTHER_ORDER_ID);
        order2.setDishes(dishes2);

        // Define mock behavior
        given(orderRepository.findById(ORDER_ID)).willReturn(Optional.ofNullable(order1));
        doNothing().when(orderRepository).delete(order1);

        given(orderRepository.findById(OTHER_ORDER_ID)).willReturn(Optional.ofNullable(order2));
        doNothing().when(orderRepository).delete(order2);

        given(orderRepository.findById(NON_EXISTENT_ORDER_ID)).willReturn(Optional.empty());

    }

    @Test
    public void testDeleteOne() {
        Boolean success = orderService.deleteOne(ORDER_ID);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepository, times(1)).delete(order1);
        assertEquals(true, success);
    }

    @Test
    public void testDeleteOne_OrderStillActive() {
        Boolean success = orderService.deleteOne(OTHER_ORDER_ID);

        verify(orderRepository, times(1)).findById(OTHER_ORDER_ID);
        verify(orderRepository, times(0)).delete(order1); // Method should have returned by now
        assertEquals(false, success);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteOne_NonExistentOrderId() {
        Boolean success = orderService.deleteOne(NON_EXISTENT_ORDER_ID);

        System.out.println("You shouldn't be able to come here");
    }
}
