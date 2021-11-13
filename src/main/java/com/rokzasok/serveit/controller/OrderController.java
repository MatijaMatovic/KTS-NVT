package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.OrderDTOToOrderConverter;
import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.model.Order;
import com.rokzasok.serveit.service.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    final
    IOrderService orderService;

    final
    OrderDTOToOrderConverter orderConverter;

    public OrderController(IOrderService orderService, OrderDTOToOrderConverter orderConverter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDTO addOrder(@RequestBody OrderDTO newOrderDTO) {
        Order newOrder = orderConverter.convert(newOrderDTO);
        newOrder = orderService.save(newOrder);
        newOrderDTO.setId(newOrder.getId());
        return newOrderDTO;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        return new ResponseEntity<>(
                orders.stream().map(OrderDTO::new).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> findOrderByID(@PathVariable Integer id) {
        Order theOne = orderService.findOne(id);
        if (theOne == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with given ID not found");

        return new ResponseEntity<>(new OrderDTO(theOne), HttpStatus.OK);
    }

    @GetMapping(value = "/all-by-waiter/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getAllOrdersByWaiter(@PathVariable Integer id) {
        //TODO: Check if waiter has to be JOIN FETCH-ed
        //TODO: Check if this should only return unfinished orders
        List<OrderDTO> waitersOrders
                = orderService.findAll()
                .stream()
                .filter(order -> Objects.equals(order.getWaiter().getId(), id))
                .map(OrderDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(waitersOrders, HttpStatus.OK);

    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> editOrder(@RequestBody OrderDTO editedOrderDTO) {
        //TODO: Check later if anything should be adapted here later
        int orderId = editedOrderDTO.getId();

        Order newOrder = orderConverter.convert(editedOrderDTO);
        newOrder.setId(orderId);

        Order oldOrder = orderService.findOne(orderId);
        if (oldOrder == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with given ID not found");

        oldOrder = newOrder;
        oldOrder = orderService.save(oldOrder);
        return new ResponseEntity<>(new OrderDTO(oldOrder), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Integer id) {
        try {
            Boolean deleted = orderService.deleteOne(id);
            if (!deleted)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "You cannot delete an order that's already in progress");
        }
        catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Requested order not found");
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
