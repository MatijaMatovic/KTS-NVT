package com.rokzasok.serveit.controller.matija.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rokzasok.serveit.converters.OrderDTOToOrderConverter;
import com.rokzasok.serveit.converters.UserToUserDTO;
import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.service.impl.*;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderServiceUnitTest {
    private static final String URL_PREFIX = "/api/orders";
    private static final Integer NEW_ORDER_ID = 55;
    private static final Integer USER_ID = 1;
    private static final Integer NON_EXISTING_ORDER_ID = 420;
    private static final MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype()
    );

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private EasyRandom generator;

    @Autowired
    WebApplicationContext context;

    @Autowired
    OrderDTOToOrderConverter orderConverter;

    @MockBean
    OrderService orderService;

    @MockBean
    UserService userService;

    @MockBean
    DishPriceService dishPriceService;

    @MockBean
    DrinkPriceService drinkPriceService;

    @MockBean
    SittingTableService sittingTableService;

    UserDTO waiterDTO;
    User waiter;
    DishOrderItem dish;
    DrinkOrderItem drink;
    Order order;
    OrderDTO orderDTO;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mapper = new ObjectMapper();
        generator = new EasyRandom();

        waiterDTO = generator.nextObject(UserDTO.class);
        waiterDTO.setType(UserType.WAITER);
        waiterDTO.setId(USER_ID);

        waiter = new UserToUserDTO().convert(waiterDTO);

        dish = generator.nextObject(DishOrderItem.class);
        drink = generator.nextObject(DrinkOrderItem.class);

        order = generator.nextObject(Order.class);
        order.setId(NEW_ORDER_ID);
        order.setWaiter(waiter);
        order.getDishes().add(dish);
        order.getDrinks().add(drink);

        orderDTO = new OrderDTO(order);

        List<Order> orders = List.of(order);

        given(orderService.save(any())).willReturn(order);
        given(orderService.findAll()).willReturn(orders);
        given(orderService.findOne(NEW_ORDER_ID)).willReturn(order);
        given(orderService.findOne(NON_EXISTING_ORDER_ID)).willReturn(null);
        given(orderService.deleteOne(NEW_ORDER_ID)).willReturn(true);
        given(orderService.deleteOne(NON_EXISTING_ORDER_ID)).willThrow(EntityNotFoundException.class);
        given(userService.findOne(waiterDTO.getId())).willReturn(waiter);
        given(userService.findOne(dish.getCook().getId())).willReturn(dish.getCook());
        given(userService.findOne(drink.getBartender().getId())).willReturn(drink.getBartender());
        given(dishPriceService.findOne(dish.getDish().getId())).willReturn(dish.getDish());
        given(drinkPriceService.findOne(drink.getDrink().getId())).willReturn(drink.getDrink());
        given(sittingTableService.findOne(any())).willReturn(order.getSittingTable());
    }

    @Test
    public void testAddOrder() throws Exception {
        String json = mapper.writeValueAsString(orderDTO);

        mockMvc.perform(post(URL_PREFIX + "/create")
                        .contentType(contentType)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()));


        verify(orderService, times(1)).save(any());
        verify(userService, times(1)).findOne(waiterDTO.getId());
        verify(userService, times(1)).findOne(dish.getCook().getId());
        verify(userService, times(1)).findOne(drink.getBartender().getId());
        verify(dishPriceService, times(1)).findOne(dish.getDish().getId());
        verify(drinkPriceService, times(1)).findOne(drink.getDrink().getId());

    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId())));

        verify(orderService, times(1)).findAll();
    }

    @Test
    public void testFindOne() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/one/" + NEW_ORDER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()));

        verify(orderService, times(1)).findOne(order.getId());
    }

    @Test
    public void testFindOne_NonExistingID() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/one/" + NON_EXISTING_ORDER_ID))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).findOne(NON_EXISTING_ORDER_ID);
    }

    @Test
    public void testFindOneByWaiter() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/all-by-waiter/" + USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[*].waiterID").value(hasItem(USER_ID)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(NEW_ORDER_ID)));

        verify(orderService, times(1)).findAll();
    }

    @Test
    public void testDeleteOrder() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/delete/" + NEW_ORDER_ID))
                .andExpect(status().isOk());

        verify(orderService, times(1)).deleteOne(NEW_ORDER_ID);
    }

    @Test
    public void testDeleteOrder_NonExistingID() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/delete/" + NON_EXISTING_ORDER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testEditOrder() throws Exception {
        OrderDTO oldOrderDTO = new OrderDTO(order);
        oldOrderDTO.setId(77);

        String newNote = "Some new note";
        OrderDTO newOrderDTO = new OrderDTO(order);
        newOrderDTO.setId(77);
        newOrderDTO.setNote(newNote);

        Order oldOrder = orderConverter.convert(oldOrderDTO);
        oldOrder.setId(77);
        Order newOrder = orderConverter.convert(newOrderDTO);
        newOrder.setId(77);

        given(orderService.save(newOrder)).willReturn(newOrder);
        given(orderService.save(oldOrder)).willReturn(newOrder);
        given(orderService.findOne(77)).willReturn(oldOrder);

        String json = mapper.writeValueAsString(newOrderDTO);
        mockMvc.perform(put(URL_PREFIX + "/edit").contentType(contentType)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(77))
                .andExpect(jsonPath("$.note").value(newNote));

        verify(orderService, times(1)).save(newOrder);
    }
}
