package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.OrderDTOToOrderConverter;
import com.rokzasok.serveit.converters.OrderItemDTOToDishOrDrinkItem;
import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.dto.OrderItemDTO;
import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.service.IOrderService;
import com.rokzasok.serveit.converters.OrderToOrderDTO;

import com.rokzasok.serveit.service.impl.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
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
  
    final 
    OrderToOrderDTO orderToOrderDTO;

    final DrinkOrderItemService drinkOrderItemService;
    final DishOrderItemService dishOrderItemService;
    final OrderItemDTOToDishOrDrinkItem converter;

    public OrderController(IOrderService orderService, OrderDTOToOrderConverter orderConverter, OrderToOrderDTO orderToOrderDTO, UserService userService, DrinkOrderItemService drinkOrderItemService, DishOrderItemService dishOrderItemService, OrderItemDTOToDishOrDrinkItem converter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
        this.orderToOrderDTO = orderToOrderDTO;
        this.drinkOrderItemService = drinkOrderItemService;
        this.dishOrderItemService = dishOrderItemService;
        this.converter = converter;
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
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Integer id) throws Exception {
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

    @GetMapping(value = "/not-finished-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getNotFinishedOrders() {
        List<Order> orders = orderService.getNotFinishedOrders();
        return new ResponseEntity<>(
                orders.stream().map(OrderDTO::new).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    /***
     * Adds one OrderItem to existing unfinished Order
     * author: isidora-stanic
     * authorized: WAITER
     *
     * @param orderId id of the order
     * @param orderItemDTO dto of new ordered item
     * @return changed order dto
     */
    //TODO:dodati za svako find all provjeru za null, i za konvertere, ako je npr kad je nesto created cook mora
    //biti null i onda ce bacati exception ovdje jer je user null
    @PostMapping(value = "/{orderId}/add-order-item")
    public ResponseEntity<OrderDTO> addOrderItem(@PathVariable Integer orderId, @RequestBody OrderItemDTO orderItemDTO) {
        Order o = orderService.findOne(orderId);
        if (o.getStatus().equals(OrderStatus.FINISHED)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        OrderItemDTO.OrderItemType type = orderItemDTO.getItemType();

        if (type.equals(OrderItemDTO.OrderItemType.DRINK)){
            DrinkOrderItem drinkOrderItem = converter.convertToDrinkItem(orderItemDTO);
            DrinkOrderItem drinkOrderItemS = drinkOrderItemService.save(drinkOrderItem);
            o.getDrinks().add(drinkOrderItemS);
        } else {
            DishOrderItem dishOrderItem = converter.convertToDishItem(orderItemDTO);
            DishOrderItem dishOrderItemS = dishOrderItemService.save(dishOrderItem);
            o.getDishes().add(dishOrderItemS);
        }
        orderService.save(o);

        return new ResponseEntity<>(orderToOrderDTO.convert(o), HttpStatus.OK);
    }

    /***
     * Adds list of OrderItems to existing unfinished Order
     * author: isidora-stanic
     * authorized: WAITER
     *
     * @param orderId id of the order
     * @param orderItemDTOs list of dtos of new ordered items
     * @return changed order dto
     */
    //TODO:dodati za svako find all provjeru za null, i za konvertere, ako je npr kad je nesto created cook mora
    //biti null i onda ce bacati exception ovdje jer je user null
    @PostMapping(value = "/{orderId}/add-order-items")
    public ResponseEntity<OrderDTO> addOrderItems(@PathVariable Integer orderId, @RequestBody List<OrderItemDTO> orderItemDTOs) {
        Order o = orderService.findOne(orderId);
        if (o.getStatus().equals(OrderStatus.FINISHED)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (OrderItemDTO orderItemDTO : orderItemDTOs){
            OrderItemDTO.OrderItemType type = orderItemDTO.getItemType();

            if (type.equals(OrderItemDTO.OrderItemType.DRINK)){
                DrinkOrderItem drinkOrderItem = converter.convertToDrinkItem(orderItemDTO);
                DrinkOrderItem drinkOrderItemS = drinkOrderItemService.save(drinkOrderItem);
                o.getDrinks().add(drinkOrderItemS);
            } else {
                DishOrderItem dishOrderItem = converter.convertToDishItem(orderItemDTO);
                DishOrderItem dishOrderItemS = dishOrderItemService.save(dishOrderItem);
                o.getDishes().add(dishOrderItemS);
            }
        }
        orderService.save(o);

        return new ResponseEntity<>(orderToOrderDTO.convert(o), HttpStatus.OK);
    }



    /***
     * Removes drink order item from order
     * author: isidora-stanic
     * authorized: WAITER
     *
     * @param orderId id of order
     * @param orderItemId id of drink order item which is being deleted
     * @return dto of changed order
     */
    //TODO:sve isto vazi kao i za sledecu metodu
    @DeleteMapping(value = "/{orderId}/delete-drink-order-item/{orderItemId}")
    public ResponseEntity<OrderDTO> deleteDrinkOrderItem(@PathVariable Integer orderId, @PathVariable Integer orderItemId) {
        Order o = orderService.findOne(orderId);
        if (o.getStatus().equals(OrderStatus.FINISHED)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DrinkOrderItem ditem = drinkOrderItemService.findOne(orderItemId);
        o.getDrinks().remove(ditem);
        orderService.save(o);
        drinkOrderItemService.deleteOne(orderItemId);

        return new ResponseEntity<>(orderToOrderDTO.convert(o), HttpStatus.OK);
    }

    /***
     * Removes dish order item from order
     * author: isidora-stanic
     * authorized: WAITER
     *
     * @param orderId id of order
     * @param orderItemId id of drink order item which is being deleted
     * @return dto of changed order
     */
    //TODO: dodati provjere da li find one vraca null, dodati to da se moze obrisati samo ako je item status CREATED, mozda promijeniti da metoda vraca boolena
    //ako ne to, onda izmjeniti u kontroleru da ne puca ako je za neki item kuvar null, jer stvari koje se brisu su created i nemaju kuvara

    @DeleteMapping(value = "/{orderId}/delete-dish-order-item/{orderItemId}")
    public ResponseEntity<OrderDTO> deleteDishOrderItem(@PathVariable Integer orderId, @PathVariable Integer orderItemId) {
        Order o = orderService.findOne(orderId);
        if (o.getStatus().equals(OrderStatus.FINISHED)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DishOrderItem ditem = dishOrderItemService.findOne(orderItemId);
        o.getDishes().remove(ditem);
        orderService.save(o);
        dishOrderItemService.deleteOne(orderItemId);

        return new ResponseEntity<>(orderToOrderDTO.convert(o), HttpStatus.OK);
    }
}
