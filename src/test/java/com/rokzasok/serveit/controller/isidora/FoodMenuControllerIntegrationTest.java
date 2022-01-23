package com.rokzasok.serveit.controller.isidora;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.dto.FoodMenuDTO;
import com.rokzasok.serveit.dto.JwtAuthenticationRequest;
import com.rokzasok.serveit.dto.UserTokenState;
import com.rokzasok.serveit.exceptions.FoodMenuNotFoundException;
import com.rokzasok.serveit.model.DishCategory;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.repository.FoodMenuRepository;
import com.rokzasok.serveit.service.impl.FoodMenuService;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FoodMenuControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FoodMenuService service;

    @Autowired
    private FoodMenuRepository repository;

    private static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    TestRestTemplate dispatcher;
    private String accessToken;

    @Before
    public void login() {
        JwtAuthenticationRequest loginDto = new JwtAuthenticationRequest(
                "managerko","password"
        );

        ResponseEntity<UserTokenState> response = dispatcher.postForEntity("/auth/login", loginDto, UserTokenState.class);
        UserTokenState user = response.getBody();
        accessToken = user.getAccessToken();
    }

    @Test
    public void testCreate_IdNull_ShouldReturnNewDTO_OK_200() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/create";

        FoodMenuDTO menuDTO = FoodMenuDTO.builder()
                .id(null)
                .date(LocalDate.now())
                .dishes(new ArrayList<>())
                .build();
        HttpEntity<FoodMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, menuDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 4, responseEntity.getBody().getId());
        assertEquals(menuDTO.getDate(), responseEntity.getBody().getDate());
        assertEquals(menuDTO.getDishes().size(), responseEntity.getBody().getDishes().size());
    }

    @Test
    public void testCreate_IdNotNull_ShouldReturnNewDTO_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/create";

        FoodMenuDTO menuDTO = FoodMenuDTO.builder()
                .id(9)
                .date(LocalDate.now())
                .dishes(new ArrayList<>())
                .build();
        HttpEntity<FoodMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, menuDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 4, responseEntity.getBody().getId());
        assertEquals(menuDTO.getDate(), responseEntity.getBody().getDate());
        assertEquals(menuDTO.getDishes().size(), responseEntity.getBody().getDishes().size());
    }

    @Test
    public void testCreate_IdExisting_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/create";

        FoodMenuDTO menuDTO = FoodMenuDTO.builder()
                .id(1)
                .date(LocalDate.now())
                .dishes(new ArrayList<>())
                .build();
        HttpEntity<FoodMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, menuDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testOne_IdExisting_ShouldReturnDTO_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/one/1";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        LinkedHashMap body = (LinkedHashMap) responseEntity.getBody();

        assertNotNull(body.get("id"));
        assertEquals((Integer) 1, body.get("id"));
        List<Integer> dateList = (ArrayList<Integer>) body.get("date");
        assertEquals(LocalDate.of(2021, 11, 6), LocalDate.of(dateList.get(0), dateList.get(1), dateList.get(2)));
        assertEquals(3, ((ArrayList<Void>)body.get("dishes")).size());
    }

    @Test
    public void testOne_IdNotExisting_ShouldThrow_NotFound_404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/one/44";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAll_ShouldReturnList_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/all";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertEquals(3, ((ArrayList<Void>)responseEntity.getBody()).size());}

    @Test
    public void testEdit_IdExisting_ShouldReturnChangedDTO_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/edit/3";

        FoodMenuDTO menuDTO = FoodMenuDTO.builder()
                .id(3)
                .date(LocalDate.now())
                .dishes(new ArrayList<>())
                .build();
        HttpEntity<FoodMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, menuDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 3, responseEntity.getBody().getId());
        assertEquals(menuDTO.getDate(), responseEntity.getBody().getDate());
        assertEquals(menuDTO.getDishes().size(), responseEntity.getBody().getDishes().size());
    }

    @Test
    public void testEdit_IdNotExisting_ShouldThrow_NotFound_404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/edit/55";

        FoodMenuDTO menuDTO = FoodMenuDTO.builder()
                .id(55)
                .date(LocalDate.now())
                .dishes(new ArrayList<>())
                .build();
        HttpEntity<FoodMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO, headers);
        ResponseEntity<FoodMenuNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, menuDTOHttpEntity, FoodMenuNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturnTrue_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/delete/2";

        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, entity, Boolean.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertTrue(responseEntity.getBody());
    }

    @Test
    public void testDelete_IdNotExisting_ShouldThrow_NotFound_404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/delete/55";

        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<FoodMenuNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, entity, FoodMenuNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testLast_ShouldReturnDTO_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/last";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        LinkedHashMap body = (LinkedHashMap) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(body);
        List<Integer> dateList = (ArrayList<Integer>) body.get("date");
        assertEquals(LocalDate.of(2021, 12, 6), LocalDate.of(dateList.get(0), dateList.get(1), dateList.get(2)));
        assertEquals(2, (int) body.get("id"));
    }

    @Test
    public void testLast_NoFoodMenuInDb_ShouldThrow_NotFound_404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // deleting every menu
        String url1 = "/api/food-menu/delete/1";
        ResponseEntity<Boolean> responseEntity1 = testRestTemplate.exchange(url1, HttpMethod.DELETE, entity, Boolean.class);
        String url2 = "/api/food-menu/delete/2";
        ResponseEntity<Boolean> responseEntity2 = testRestTemplate.exchange(url2, HttpMethod.DELETE, entity, Boolean.class);
        String url3 = "/api/food-menu/delete/3";
        ResponseEntity<Boolean> responseEntity3 = testRestTemplate.exchange(url3, HttpMethod.DELETE, entity, Boolean.class);

        String url = "/api/food-menu/last";
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testCopyCreate_IdExisting_ShouldReturnNewDTO_OK_200() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/copy-create/1";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        FoodMenu oldMenu = service.findOne(1);

        LinkedHashMap body = (LinkedHashMap) responseEntity.getBody();

        assertNotNull(body.get("id"));
        assertNotEquals((Integer) 1, body.get("id"));

        List<Integer> dateList = (ArrayList<Integer>) body.get("date");
        assertTrue(oldMenu.getDate().isBefore(LocalDate.of(dateList.get(0), dateList.get(1), dateList.get(2))));

        ArrayList<Object> dishes = ((ArrayList<Object>) body.get("dishes"));

        assertEquals(oldMenu.getDishes().size(), dishes.size());
        List<DishPrice> list = new ArrayList<>(oldMenu.getDishes());
        for (int i = 0; i < oldMenu.getDishes().size(); ++i) {
            System.out.println(dishes.get(i));
            System.out.println(dishes.get(i).getClass());
            LinkedHashMap price = (LinkedHashMap) dishes.get(i);
            assertEquals(list.get(i).getId(), price.get("dishId"));
        }
    }

    @Test
    public void testCopyCreate_IdNotExisting_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/copy-create/55";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, entity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddDish_IdExisting_DishExistingNotInMenu_ShouldReturnDTO_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/add-dish";

        int oldSize = service.findOne(1).getDishes().size();

        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(1)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());

        DishPriceDTO addedDishPrice = null;
        for (DishPriceDTO dp : responseEntity.getBody().getDishes()) {
            if (dp.getDishId() == 1){
                addedDishPrice = dp;
            }
        }
        assertNotNull(addedDishPrice);
        assertEquals(priceDTO.getDishId(), addedDishPrice.getDishId());
        assertEquals(priceDTO.getPrice(), addedDishPrice.getPrice());
        assertEquals(priceDTO.getPriceDate(), addedDishPrice.getPriceDate());

        assertEquals(oldSize + 1, responseEntity.getBody().getDishes().size());
    }

    @Test
    public void testAddDish_IdNotExisting_DishExistingNotInMenu_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/55/add-dish";

        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(1)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddDish_IdExisting_DishNotExisting_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/add-dish";

        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(55)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddDish_IdExisting_DishExisting_InMenu_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/add-dish";

        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(4)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteDish_IdExisting_DishInMenu_ShouldReturnDTO_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/delete-dish/4";

        int oldSize = service.findOne(1).getDishes().size();

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, entity, FoodMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());

        for (DishPriceDTO dp : responseEntity.getBody().getDishes()) {
            assertNotEquals(4, (int) dp.getDishId());
        }

        assertEquals(oldSize - 1, responseEntity.getBody().getDishes().size());
    }

    @Test
    public void testDeleteDish_IdNotExisting_DishInMenu_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/55/delete-dish/4";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, entity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteDish_IdExisting_DishNotInMenu_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/delete-dish/1";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, entity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteDish_IdExisting_PriceNotExisting_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/delete-dish/55";

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, entity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testEditDishPrice_IdExisting_DishInMenu_ShouldReturnDTO_OK_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/edit-dish-price";
        int oldSize = service.findOne(1).getDishes().size();
        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(4)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());

        int count = 0;
        DishPriceDTO newPrice = null;
        for (DishPriceDTO dp : responseEntity.getBody().getDishes()) {
            if (dp.getDishId().equals(4)) {
                newPrice = dp;
                count++;
            }
        }
        assertEquals(1, count); // samo jedna cena za jelo

        assertEquals(oldSize, responseEntity.getBody().getDishes().size());
        assertNotNull(newPrice);
        assertEquals(priceDTO.getPrice(), newPrice.getPrice());
        assertEquals(priceDTO.getPriceDate(), newPrice.getPriceDate());
    }

    @Test
    public void testEditDishPrice_IdNotExisting_DishInMenu_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/55/edit-dish-price";
        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(4)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testEditDishPrice_IdExisting_DishNotInMenu_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/edit-dish-price";
        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(1)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testEditDishPrice_IdExisting_PriceNotExisting_ShouldThrow_BadRequest_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);

        String url = "/api/food-menu/1/edit-dish-price";
        DishPriceDTO priceDTO = DishPriceDTO.builder()
                .id(null)
                .dishId(55)
                .dishCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .dishCategory(DishCategory.BREAKFAST)
                .build();
        HttpEntity<DishPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO, headers);
        ResponseEntity<FoodMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, FoodMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    //-------------------------------------
    public String json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(obj);
    }

}
