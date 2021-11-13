package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.OrderToOrderDTO;
import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.model.Order;
import com.rokzasok.serveit.model.OrderStatus;
import com.rokzasok.serveit.service.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    final IOrderService orderService;
    final OrderToOrderDTO orderToOrderDTO;

    public OrderController(IOrderService orderService, OrderToOrderDTO orderToOrderDTO) {
        this.orderService = orderService;
        this.orderToOrderDTO = orderToOrderDTO;
    }

    //todo: Da li ovde raditi proveru da li je status NOT_FINISHED?
    @PutMapping(value = "/deliver-order/{id}", consumes = "application/json")
    public ResponseEntity<OrderDTO> deliverOrder(@PathVariable Integer id) {
        Order order = orderService.findOne(id);

        if (order.getStatus() == OrderStatus.FINISHED) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        order.setStatus(OrderStatus.FINISHED);
        order = orderService.save(order);

        return new ResponseEntity<>(orderToOrderDTO.convert(order), HttpStatus.OK);
    }
}
