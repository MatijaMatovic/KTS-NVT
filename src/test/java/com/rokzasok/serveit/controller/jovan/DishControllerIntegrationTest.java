package com.rokzasok.serveit.controller.jovan;

import com.rokzasok.serveit.constants.DishConstants;
import com.rokzasok.serveit.converters.DishToDishDTO;
import com.rokzasok.serveit.dto.DishDTO;
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


    @Test
    public void testGetDishes_ReturnsStatusOkAndListOfDishesWithCorrectNumberOfInstances() {
        ResponseEntity<DishDTO[]> responseEntity = restTemplate.getForEntity("/api/dishes/", DishDTO[].class);

        DishDTO[] dishes = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(DishConstants.NUMBER_OF_INSTANCES, dishes != null ? dishes.length : 0);
    }

    @Test
    public void getDish_CorrectID_ReturnsStatusOkAndCorrectDishDto() {
        String url = String.format("/api/dishes/%d", DishConstants.CORRECT_ID);
        ResponseEntity<DishDTO> responseEntity = restTemplate.getForEntity(url, DishDTO.class);

        DishDTO dishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert dishDTO != null;
        assertEquals(DishConstants.CORRECT_ID, dishDTO.getId());
    }

    @Test
    public void getDish_WrongID_ReturnsStatusNotFound() {
        String url = String.format("/api/dishes/%d", DishConstants.WRONG_ID);
        ResponseEntity<DishDTO> responseEntity = restTemplate.getForEntity(url, DishDTO.class);

        DishDTO dishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(dishDTO);
    }

    @Test
    public void addNewDish_NewIdDishReturnsStatusOkAndCorrectDishDto() {
        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.NEW_ID_DISH);
        ResponseEntity<DishDTO> responseEntity = restTemplate.postForEntity("/api/dishes/", dishDTO, DishDTO.class);

        DishDTO savedDishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert savedDishDTO != null;
        assertEquals(dishDTO, savedDishDTO);

    }

    @Test
    public void addNewDish_ExistingIdDish_ReturnsStatusBadRequest() {
        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.EXISTING_ID_DISH);
        ResponseEntity<DishDTO> responseEntity = restTemplate.postForEntity("/api/dishes/", dishDTO, DishDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void addNewDish_NoIdDish_ReturnsStatusOkAndCorrectDishDto() {
        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.NO_ID_DISH);
        ResponseEntity<DishDTO> responseEntity = restTemplate.postForEntity("/api/dishes/", dishDTO, DishDTO.class);

        DishDTO savedDishDTO = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert savedDishDTO != null;
        dishDTO.setId(DishConstants.NUMBER_OF_INSTANCES + 1);
        assertEquals(dishDTO, savedDishDTO);

    }

    @Test
    public void updateDish_ExistingDish_ReturnsStatusOkAndCorrectDishDto() {
        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.EXISTING_ID_DISH);
        HttpEntity<DishDTO> existingDishDTO = new HttpEntity<>(dishDTO);
        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange("/api/dishes", HttpMethod.PUT, existingDishDTO, DishDTO.class);

        DishDTO updatedDishDTO = responseEntity.getBody();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert updatedDishDTO != null;
        assertEquals(dishDTO, updatedDishDTO);
    }

    @Test
    public void updateDish_WrongDish_ReturnsStatusBadRequest() {
        DishDTO dishDTO = dishToDishDTO.convert(DishConstants.NEW_ID_DISH);
        HttpEntity<DishDTO> existingDishDTO = new HttpEntity<>(dishDTO);
        ResponseEntity<DishDTO> responseEntity = restTemplate.exchange("/api/dishes", HttpMethod.PUT, existingDishDTO, DishDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteDish_ExistingID_ReturnsStatusOkAndTrue() {
        String url = String.format("/api/dishes/%d", DishConstants.CORRECT_ID);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Boolean.class);
        Boolean success = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, success);
    }

    @Test
    public void deleteDish_WrongID_ReturnsStatusBadRequestAndFalse() {
        String url = String.format("/api/dishes/%d", DishConstants.WRONG_ID);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Boolean.class);

        Boolean success = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(false, success);
    }
}