package com.rokzasok.serveit.controller.klimenta.integration;

import com.rokzasok.serveit.dto.JwtAuthenticationRequest;
import com.rokzasok.serveit.dto.UserTokenState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DrinkOrderItemControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/drink-order-items";
    private static final Integer DRINK_ORDER_ITEM_ID = 1;
    private static final Integer READY_DRINK_ORDER_ITEM_ID = 2;
    private static final Integer IN_PROGRESS_DRINK_ORDER_ITEM_ID = 4;
    private static final Integer BARTENDER_ID = 8;
    private static final Integer INVALID_BARTENDER_ID = 9;
    private static final Integer COOK_ID = 4;
    private static final Integer NON_EXISTING_ID = 111;

    @Autowired
    TestRestTemplate dispatcher;

    private String accessToken;

    @Before
    public void login() {
        JwtAuthenticationRequest loginDto = new JwtAuthenticationRequest(
                "Gonko","password"
        );

        ResponseEntity<UserTokenState> response = dispatcher.postForEntity("/auth/login", loginDto, UserTokenState.class);
        UserTokenState user = response.getBody();
        accessToken = user.getAccessToken();
    }

    @Test
    public void testAcceptDrinkOrderItem_NonExistingDrinkOrderItem(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAcceptDrinkOrderItem_NonExistingBartender(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(NON_EXISTING_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAcceptDrinkOrderItem_WrongItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + READY_DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAcceptDrinkOrderItem_CorrectDrinkOrderItem_CorrectCook_CorrectItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/accept-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCompleteDrinkOrderItem_NonExistingDrinkOrderItem(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + NON_EXISTING_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDrinkOrderItem_NonExistingBartender(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(NON_EXISTING_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDrinkOrderItem_WrongItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDrinkOrderItem_BartenderWhoDidNotAcceptItemChangingStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(INVALID_BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + IN_PROGRESS_DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCompleteDrinkOrderItem_CookChangingStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(COOK_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + IN_PROGRESS_DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCompleteDrinkOrderItem_CorrectDrinkOrderItem_CorrectBartender_CorrectItemStatus(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Integer> request = new HttpEntity<>(BARTENDER_ID, headers);
        ResponseEntity<Object> response = dispatcher.exchange(
                URL_PREFIX + "/complete-drink-order/" + IN_PROGRESS_DRINK_ORDER_ITEM_ID, HttpMethod.PUT, request, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}

