package com.rokzasok.serveit.controller.jovan;

import com.rokzasok.serveit.constants.DishOrderItemConstants;
import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemDTO;
import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.JwtAuthenticationRequest;
import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.dto.UserTokenState;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DishOrderItemControllerIntegrationTest {

    private String getAccessToken(String username, String password) {
        ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest(username, password), UserTokenState.class);
        return String.format("Bearer %s", Objects.requireNonNull(responseEntity.getBody()).getAccessToken());
    }

    private String getAccessToken(UserType userType) {
        String password = "$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq";

        switch (userType) {
            case WAITER:
                return getAccessToken("potrcko", password);
            case COOK:
                return getAccessToken("kuvarko", password);
            default:
                return "";
        }
    }

    @Autowired
    private DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void deliverOrderItem_ReturnsOK() {
        String url = String.format("/api/dish-order-items/deliver-item/%d", DishOrderItemConstants.ID_WITH_READY_STATUS);
        ResponseEntity<DishOrderItemDTO> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, DishOrderItemDTO.class);

        DishOrderItemDTO dishOrderItemDTO = responseEntity.getBody();

        assert dishOrderItemDTO != null;
        assertEquals(DishOrderItemConstants.ID_WITH_READY_STATUS, dishOrderItemDTO.getId());
        assertEquals(ItemStatus.DELIVERED, dishOrderItemDTO.getStatus());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deliverOrderItem_Returns_ReturnsOK() {
        String url = String.format("/api/dish-order-items/deliver-item/%d", DishOrderItemConstants.ID_WITH_READY_STATUS);
        ResponseEntity<DishOrderItemDTO> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, DishOrderItemDTO.class);

        DishOrderItemDTO dishOrderItemDTO = responseEntity.getBody();

        assert dishOrderItemDTO != null;
        assertEquals(DishOrderItemConstants.ID_WITH_READY_STATUS, dishOrderItemDTO.getId());
        assertEquals(ItemStatus.DELIVERED, dishOrderItemDTO.getStatus());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }



    @Test
    public void completeDishOrderItem() {
        String url = String.format("/api/dish-order-items/complete-dish-order/%d", DishOrderItemConstants.CORRECT_ID);
        ResponseEntity<DishOrderItemDTO> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, DishOrderItemDTO.class);

        DishOrderItemDTO dishOrderItemDTO = responseEntity.getBody();

        assert dishOrderItemDTO != null;
        assertEquals(DishOrderItemConstants.CORRECT_ID, dishOrderItemDTO.getId());
    }

    @Test
    public void acceptDishOrderItem() {
        String url = String.format("/api/dish-order-items/accept-dish-order/%d", DishOrderItemConstants.CORRECT_ID);
        OrderItemWorkerDTO orderItemWorkerDTO = new OrderItemWorkerDTO(DishOrderItemConstants.CORRECT_ID, DishOrderItemConstants.TEST_COOK_ID);
        HttpEntity<OrderItemWorkerDTO> requestEntity = new HttpEntity<>(orderItemWorkerDTO);

        ResponseEntity<DishOrderItemDTO> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, DishOrderItemDTO.class);
        DishOrderItemDTO dishOrderItemDTO = responseEntity.getBody();

        assert dishOrderItemDTO != null;
        assertEquals(DishOrderItemConstants.CORRECT_ID, dishOrderItemDTO.getId());

    }

    @Test
    public void getCookOrders() {
        String url = String.format("/api/dish-order-items/cook-orders/%d", DishOrderItemConstants.TEST_COOK_ID);
        ResponseEntity<DishOrderItemDTO[]> responseEntity = restTemplate.getForEntity(url, DishOrderItemDTO[].class);

        DishOrderItemDTO[] dishOrderItems = responseEntity.getBody();

        assertEquals(DishOrderItemConstants.NUMBER_OF_ITEM_ORDERS_FOR_TEST_COOK_ID, dishOrderItems != null ? dishOrderItems.length : 0);
    }
}