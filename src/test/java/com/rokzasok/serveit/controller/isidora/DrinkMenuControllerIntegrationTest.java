package com.rokzasok.serveit.controller.isidora;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.exceptions.DrinkMenuNotFoundException;
import com.rokzasok.serveit.model.DrinkCategory;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkMenuRepository;
import com.rokzasok.serveit.service.impl.DrinkMenuService;
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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DrinkMenuControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DrinkMenuService service;

    @Autowired
    private DrinkMenuRepository repository;

    private static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    public void testCreate_IdNull_ShouldReturnNewDTO_OK_200() throws Exception {
        String url = "/api/drink-menu/create";

        DrinkMenuDTO menuDTO = DrinkMenuDTO.builder()
                .id(null)
                .date(LocalDate.now())
                .drinks(new ArrayList<>())
                .build();
        HttpEntity<DrinkMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, menuDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 3, responseEntity.getBody().getId());
        assertEquals(menuDTO.getDate(), responseEntity.getBody().getDate());
        assertEquals(menuDTO.getDrinks().size(), responseEntity.getBody().getDrinks().size());
    }

    @Test
    public void testCreate_IdNotNull_ShouldReturnNewDTO_OK_200() {
        String url = "/api/drink-menu/create";

        DrinkMenuDTO menuDTO = DrinkMenuDTO.builder()
                .id(9)
                .date(LocalDate.now())
                .drinks(new ArrayList<>())
                .build();
        HttpEntity<DrinkMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, menuDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 3, responseEntity.getBody().getId());
        assertEquals(menuDTO.getDate(), responseEntity.getBody().getDate());
        assertEquals(menuDTO.getDrinks().size(), responseEntity.getBody().getDrinks().size());
    }

    @Test
    public void testCreate_IdExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/create";

        DrinkMenuDTO menuDTO = DrinkMenuDTO.builder()
                .id(1)
                .date(LocalDate.now())
                .drinks(new ArrayList<>())
                .build();
        HttpEntity<DrinkMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, menuDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testOne_IdExisting_ShouldReturnDTO_OK_200() {
        String url = "/api/drink-menu/one/1";

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());
        assertEquals(LocalDate.of(2021, 11, 6), responseEntity.getBody().getDate());
        assertEquals(2, responseEntity.getBody().getDrinks().size());
    }

    @Test
    public void testOne_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/drink-menu/one/44";

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAll_ShouldReturnList_OK_200() {
        String url = "/api/drink-menu/all";

        ResponseEntity<DrinkMenuDTO[]> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkMenuDTO[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertEquals(repository.findAll().size(), responseEntity.getBody().length);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChangedDTO_OK_200() {
        String url = "/api/drink-menu/edit/1";

        DrinkMenuDTO menuDTO = DrinkMenuDTO.builder()
                .id(1)
                .date(LocalDate.now())
                .drinks(new ArrayList<>())
                .build();
        HttpEntity<DrinkMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, menuDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());
        assertEquals(menuDTO.getDate(), responseEntity.getBody().getDate());
        assertEquals(menuDTO.getDrinks().size(), responseEntity.getBody().getDrinks().size());
    }

    @Test
    public void testEdit_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/drink-menu/edit/55";

        DrinkMenuDTO menuDTO = DrinkMenuDTO.builder()
                .id(55)
                .date(LocalDate.now())
                .drinks(new ArrayList<>())
                .build();
        HttpEntity<DrinkMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO);
        ResponseEntity<DrinkMenuNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, menuDTOHttpEntity, DrinkMenuNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturnTrue_OK_200() {
        String url = "/api/drink-menu/delete/2";

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertTrue(responseEntity.getBody());
    }

    @Test
    public void testDelete_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/drink-menu/delete/55";

        ResponseEntity<DrinkMenuNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, DrinkMenuNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testLast_NoDrinkMenuInDb_ShouldThrow_NotFound_404() {

        // deleting every menu
        String url1 = "/api/drink-menu/delete/1";
        ResponseEntity<Boolean> responseEntity1 = testRestTemplate.exchange(url1, HttpMethod.DELETE, null, Boolean.class);
        String url2 = "/api/drink-menu/delete/2";
        ResponseEntity<Boolean> responseEntity2 = testRestTemplate.exchange(url2, HttpMethod.DELETE, null, Boolean.class);

        String url = "/api/drink-menu/last";
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testCopyCreate_IdExisting_ShouldReturnNewDTO_OK_200() throws Exception {
        String url = "/api/drink-menu/copy-create/1";

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        DrinkMenu oldMenu = service.findOne(1);

        assertNotNull(responseEntity.getBody().getId());
        assertNotEquals((Integer) 1, responseEntity.getBody().getId());
        assertTrue(oldMenu.getDate().isBefore(responseEntity.getBody().getDate()));

        assertEquals(oldMenu.getDrinks().size(), responseEntity.getBody().getDrinks().size());
        List<DrinkPrice> list = new ArrayList<>(oldMenu.getDrinks());
        for (int i = 0; i < oldMenu.getDrinks().size(); ++i) {
            assertEquals(list.get(i).getId(), responseEntity.getBody().getDrinks().get(i).getId());
        }
    }

    @Test
    public void testCopyCreate_IdNotExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/copy-create/55";

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddDrink_IdExisting_DrinkExistingNotInMenu_ShouldReturnDTO_OK_200() {
        String url = "/api/drink-menu/1/add-drink";

        int oldSize = service.findOne(1).getDrinks().size();

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(3)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());

        DrinkPriceDTO addedDrinkPrice = null;
        for (DrinkPriceDTO dp : responseEntity.getBody().getDrinks()) {
            if (dp.getDrinkId() == 3){
                addedDrinkPrice = dp;
            }
        }
        assertNotNull(addedDrinkPrice);
        assertEquals(priceDTO.getDrinkId(), addedDrinkPrice.getDrinkId());
        assertEquals(priceDTO.getPrice(), addedDrinkPrice.getPrice());
        assertEquals(priceDTO.getPriceDate(), addedDrinkPrice.getPriceDate());

        assertEquals(oldSize + 1, responseEntity.getBody().getDrinks().size());
    }

    @Test
    public void testAddDrink_IdNotExisting_DrinkExistingNotInMenu_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/55/add-drink";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(1)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddDrink_IdExisting_DrinkNotExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/1/add-drink";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(55)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAddDrink_IdExisting_DrinkExisting_InMenu_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/1/add-drink";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(1)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteDrink_IdExisting_DrinkInMenu_ShouldReturnDTO_OK_200() {
        String url = "/api/drink-menu/1/delete-drink/1";

        int oldSize = service.findOne(1).getDrinks().size();
        System.out.println(oldSize);

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());

        for (DrinkPriceDTO dp : responseEntity.getBody().getDrinks()) {
            assertNotEquals(1, (int) dp.getDrinkId());
        }

        assertEquals(oldSize - 1, responseEntity.getBody().getDrinks().size());
    }

    @Test
    public void testDeleteDrink_IdNotExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/55/delete-drink/1";

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteDrink_IdExisting_DrinkNotInMenu_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/1/delete-drink/3";

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteDrink_IdExisting_PriceNotExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/1/delete-drink/55";

        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testEditDrinkPrice_IdExisting_DrinkInMenu_ShouldReturnDTO_OK_200() {
        String url = "/api/drink-menu/1/edit-drink-price";
        int oldSize = service.findOne(1).getDrinks().size();
        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(1)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());

        int count = 0;
        DrinkPriceDTO newPrice = null;
        for (DrinkPriceDTO dp : responseEntity.getBody().getDrinks()) {
            if (dp.getDrinkId().equals(1)) {
                newPrice = dp;
                count++;
            }
        }
        assertEquals(1, count); // samo jedna cena za jelo

        assertEquals(oldSize, responseEntity.getBody().getDrinks().size());
        assertNotNull(newPrice);
        assertEquals(priceDTO.getPrice(), newPrice.getPrice());
        assertEquals(priceDTO.getPriceDate(), newPrice.getPriceDate());
    }

    @Test
    public void testEditDrinkPrice_IdNotExisting_DrinkInMenu_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/55/edit-drink-price";
        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(4)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testEditDrinkPrice_IdExisting_DrinkNotInMenu_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/1/edit-drink-price";
        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(3)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testEditDrinkPrice_IdExisting_PriceNotExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-menu/1/edit-drink-price";
        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(55)
                .drinkCode("")
                .price(3000.00)
                .priceDate(LocalDate.now())
                .drinkCategory(DrinkCategory.NON_ALCOHOLIC)
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    // TODO: last, addDrink, copyCreate, deleteDrink, editDrinkPrice - testirati
    //-------------------------------------
    public String json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(obj);
    }

}
