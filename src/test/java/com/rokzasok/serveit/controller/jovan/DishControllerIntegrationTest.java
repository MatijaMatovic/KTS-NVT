package com.rokzasok.serveit.controller.jovan;

import com.rokzasok.serveit.constants.DishConstants;
import com.rokzasok.serveit.converters.DishToDishDTO;
import com.rokzasok.serveit.dto.DishDTO;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DishControllerIntegrationTest {

    @Autowired
    private DishToDishDTO dishToDishDTO;
    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    @Before
    public void login() {
        JwtAuthenticationRequest chefLogin = new JwtAuthenticationRequest("sefko", "password");

        ResponseEntity<UserTokenState> response = restTemplate.postForEntity("/auth/login", chefLogin, UserTokenState.class);
        UserTokenState user = response.getBody();
        accessToken = user != null ? user.getAccessToken() : null;
    }



    @Test
    public void getDishes_ReturnsStatusOkAndListOfDishesWithCorrectNumberOfInstances() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity request = new HttpEntity(null, headers);

        ResponseEntity<DishDTO[]> responseEntity = restTemplate.exchange("/api/dishes/", HttpMethod.GET, request, DishDTO[].class);


        DishDTO[] dishes = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(DishConstants.NUMBER_OF_INSTANCES, dishes != null ? dishes.length : 0);
    }


    @Test
    public void getDish_CorrectID_ReturnsStatusOkAndCorrectDishDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity request = new HttpEntity(null, headers);
        String url = String.format("/api/dishes/%d", DishConstants.CORRECT_ID);

        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, DishDTO.class);

        DishDTO dishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert dishDTO != null;
        assertEquals(DishConstants.CORRECT_ID, dishDTO.getId());
    }

    @Test
    public void getDish_WrongID_ReturnsStatusNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity request = new HttpEntity(null, headers);

        String url = String.format("/api/dishes/%d", DishConstants.WRONG_ID);
        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, DishDTO.class);

        DishDTO dishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(dishDTO);
    }

    @Test
    public void addNewDish_NewIdDishReturnsStatusOkAndCorrectDishDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.NEW_ID_DISH);

        HttpEntity<DishDTO> request = new HttpEntity<>(dishDTO, headers);

        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange("/api/dishes/", HttpMethod.POST, request, DishDTO.class);

        DishDTO savedDishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert savedDishDTO != null;
        assertEquals(dishDTO, savedDishDTO);

    }

    @Test
    public void addNewDish_ExistingIdDish_ReturnsStatusBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.EXISTING_ID_DISH);

        HttpEntity<DishDTO> request = new HttpEntity<>(dishDTO, headers);

        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange("/api/dishes/", HttpMethod.POST, request, DishDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void addNewDish_NoIdDish_ReturnsStatusOkAndCorrectDishDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.NO_ID_DISH);

        HttpEntity<DishDTO> request = new HttpEntity<>(dishDTO, headers);

        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange("/api/dishes/", HttpMethod.POST, request, DishDTO.class);

        DishDTO savedDishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert savedDishDTO != null;
        dishDTO.setId(DishConstants.NUMBER_OF_INSTANCES + 1);
        assertEquals(dishDTO, savedDishDTO);

    }

    @Test
    public void updateDish_ExistingDish_ReturnsStatusOkAndCorrectDishDto() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.EXISTING_ID_DISH);
        HttpEntity<DishDTO> existingDishDTO = new HttpEntity<>(dishDTO, headers);

        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange("/api/dishes", HttpMethod.PUT, existingDishDTO, DishDTO.class);

        DishDTO updatedDishDTO = responseEntity.getBody();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert updatedDishDTO != null;
        assertEquals(dishDTO, updatedDishDTO);
    }

    @Test
    public void updateDish_WrongDish_ReturnsStatusBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.NEW_ID_DISH);
        HttpEntity<DishDTO> existingDishDTO = new HttpEntity<>(dishDTO, headers);
        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange("/api/dishes", HttpMethod.PUT, existingDishDTO, DishDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteDish_ExistingID_ReturnsStatusOkAndTrue() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity request = new HttpEntity(null, headers);

        String url = String.format("/api/dishes/%d", DishConstants.CORRECT_ID);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, Boolean.class);
        Boolean success = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, success);
    }

    @Test
    public void deleteDish_WrongID_ReturnsStatusBadRequestAndFalse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity request = new HttpEntity(null, headers);


        String url = String.format("/api/dishes/%d", DishConstants.WRONG_ID);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, Boolean.class);

        Boolean success = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(false, success);
    }
}