package com.rokzasok.serveit.controller.jovan;

import com.rokzasok.serveit.constants.DishConstants;
import com.rokzasok.serveit.constants.DrinkConstants;
import com.rokzasok.serveit.converters.DrinkToDrinkDTO;
import com.rokzasok.serveit.dto.DishDTO;
import com.rokzasok.serveit.dto.DrinkDTO;
import com.rokzasok.serveit.dto.JwtAuthenticationRequest;
import com.rokzasok.serveit.dto.UserTokenState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DrinkControllerIntegrationTest {

    @Autowired
    private DrinkToDrinkDTO drinkToDrinkDTO;
    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    @Before
    public void login(){
        JwtAuthenticationRequest chefLogin = new JwtAuthenticationRequest("managerko", "password");

        ResponseEntity<UserTokenState> response = restTemplate.postForEntity("/auth/login", chefLogin, UserTokenState.class);
        UserTokenState user = response.getBody();
        accessToken = user != null ? user.getAccessToken() : null;
    }

    @Test
    public void getDrinks_ReturnsStatusOkAndListOfDrinksWithCorrectNumberOfInstances() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity request = new HttpEntity(null, headers);

        ResponseEntity<DrinkDTO[]> responseEntity = restTemplate.exchange("/api/drinks/", HttpMethod.GET, request, DrinkDTO[].class);


        DrinkDTO[] drinks = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(DrinkConstants.NUMBER_OF_INSTANCES, drinks != null ? drinks.length : 0);
    }


    @Test
    public void getDrink_CorrectID_ReturnsStatusOkAndCorrectDrinkDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity request = new HttpEntity(null, headers);
        String url = String.format("/api/drinks/%d", DrinkConstants.CORRECT_ID);

        ResponseEntity<DrinkDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, DrinkDTO.class);

        DrinkDTO drinkDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert drinkDTO != null;
        assertEquals(DrinkConstants.CORRECT_ID, drinkDTO.getId());
    }

    @Test
    public void getDrink_WrongID_ReturnsStatusNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity request = new HttpEntity(null, headers);

        String url = String.format("/api/drinks/%d", DrinkConstants.WRONG_ID);
        ResponseEntity<DrinkDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, DrinkDTO.class);

        DrinkDTO drinkDTO = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(drinkDTO);
    }

    @Test
    public void addNewDrink_NewIdDrinkReturnsStatusOkAndCorrectDrinkDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DrinkDTO drinkDTO = drinkToDrinkDTO.convert(DrinkConstants.NEW_ID_DRINK);

        HttpEntity<DrinkDTO> request = new HttpEntity<>(drinkDTO, headers);

        ResponseEntity<DrinkDTO> responseEntity = restTemplate.exchange("/api/drinks/", HttpMethod.POST, request, DrinkDTO.class);

        DrinkDTO savedDrinkDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert savedDrinkDTO != null;
        assertEquals(drinkDTO, savedDrinkDTO);

    }

    @Test
    public void addNewDrink_ExistingIdDrink_ReturnsStatusBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        DrinkDTO drinkDTO = drinkToDrinkDTO.convert(DrinkConstants.EXISTING_ID_DRINK);

        HttpEntity<DrinkDTO> request = new HttpEntity<>(drinkDTO, headers);

        ResponseEntity<DrinkDTO> responseEntity = restTemplate.exchange("/api/drinks/", HttpMethod.POST, request, DrinkDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void addNewDrink_NoIdDrink_ReturnsStatusOkAndCorrectDrinkDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DrinkDTO drinkDTO = drinkToDrinkDTO.convert(DrinkConstants.NO_ID_DRINK);

        HttpEntity<DrinkDTO> request = new HttpEntity<>(drinkDTO, headers);

        ResponseEntity<DrinkDTO> responseEntity = restTemplate.exchange("/api/drinks/", HttpMethod.POST, request, DrinkDTO.class);

        DrinkDTO savedDrinkDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert savedDrinkDTO != null;
        drinkDTO.setId(DrinkConstants.NUMBER_OF_INSTANCES + 1);
        assertEquals(drinkDTO, savedDrinkDTO);

    }

    @Test
    public void updateDrink_ExistingDrink_ReturnsStatusOkAndCorrectDrinkDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DrinkDTO drinkDTO = drinkToDrinkDTO.convert(DrinkConstants.EXISTING_ID_DRINK);
        HttpEntity<DrinkDTO> existingDrinkDTO = new HttpEntity<>(drinkDTO, headers);

        ResponseEntity<DrinkDTO> responseEntity = restTemplate.exchange("/api/drinks", HttpMethod.PUT, existingDrinkDTO, DrinkDTO.class);

        DrinkDTO updatedDrinkDTO = responseEntity.getBody();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert updatedDrinkDTO != null;
        assertEquals(drinkDTO, updatedDrinkDTO);
    }

    @Test
    public void updateDrink_WrongDrink_ReturnsStatusBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DrinkDTO drinkDTO = drinkToDrinkDTO.convert(DrinkConstants.NEW_ID_DRINK);
        HttpEntity<DrinkDTO> existingDrinkDTO = new HttpEntity<>(drinkDTO, headers);
        ResponseEntity<DrinkDTO> responseEntity = restTemplate.exchange("/api/drinks", HttpMethod.PUT, existingDrinkDTO, DrinkDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteDrink_ExistingID_ReturnsStatusOkAndTrue() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity request = new HttpEntity(null, headers);

        String url = String.format("/api/drinks/%d", DrinkConstants.CORRECT_ID);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, Boolean.class);
        Boolean success = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, success);
    }

    @Test
    public void deleteDrink_WrongID_ReturnsStatusBadRequestAndFalse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity request = new HttpEntity(null, headers);


        String url = String.format("/api/drinks/%d", DrinkConstants.WRONG_ID);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, Boolean.class);

        Boolean success = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(false, success);
    }
}