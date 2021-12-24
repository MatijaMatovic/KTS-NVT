package com.rokzasok.serveit.controller.klimenta.integration;
import com.rokzasok.serveit.dto.JwtAuthenticationRequest;
import com.rokzasok.serveit.dto.UserTokenState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DishOrderItemControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/dish-order-items";

    @Autowired
    TestRestTemplate dispatcher;

    private static final Integer READY_DISH_ORDER_ITEM_ID = 5;
    private static final Integer CREATED_DISH_ORDER_ITEM_ID = 1;
    private static final Integer IN_PROGRESS_DISH_ORDER_ITEM_ID = 4;

    private static final Integer COOK_ID = 4;
    private static final Integer BARTENDER_ID = 8;
    private static final Integer ILLEGAL_COOK_ID = 9;
    private static final Integer DISH_ORDER_ITEM_ID = 1;
    private static final Integer NON_EXISTING_ID = 111;
    private String accessToken;

    @Before
    public void login() {
        JwtAuthenticationRequest loginDto = new JwtAuthenticationRequest(
                "kuvarko","password"
        );

        ResponseEntity<UserTokenState> response = dispatcher.postForEntity("/auth/login", loginDto, UserTokenState.class);
        UserTokenState user = response.getBody();
        accessToken = user.getAccessToken();
    }

    @Test
    public void testAcceptDishOrderItem_NonExistingDishOrderItem(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_NonExistingCook(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(NON_EXISTING_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_WrongItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAcceptDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-dish-order/" + CREATED_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCompleteDishOrderItem_NonExistingDishOrderItem(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_NonExistingCook(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(NON_EXISTING_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_WrongItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_CookWhoDidNotAcceptItemChangingStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(ILLEGAL_COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDishOrderItem_BartenderChangingStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + READY_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCompleteDishOrderItem_CorrectDishOrderItem_CorrectCook_CorrectItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-dish-order/" + IN_PROGRESS_DISH_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
