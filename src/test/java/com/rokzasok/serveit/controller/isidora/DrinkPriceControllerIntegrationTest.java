package com.rokzasok.serveit.controller.isidora;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.exceptions.DrinkPriceNotFoundException;
import com.rokzasok.serveit.repository.DrinkPriceRepository;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DrinkPriceControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DrinkPriceService service;

    @Autowired
    private DrinkPriceRepository repository;

    private static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    public void testCreate_IdNull_ShouldReturnNewTableDTO_OK_200() throws Exception {
        String url = "/api/drink-prices/create";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(null)
                .drinkId(1)
                .drinkCode("")
                .price(300.00)
                .priceDate(LocalDate.now())
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkPriceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, DrinkPriceDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 5, responseEntity.getBody().getId());
        assertEquals(priceDTO.getDrinkId(), responseEntity.getBody().getDrinkId());
        assertEquals(priceDTO.getPrice(), responseEntity.getBody().getPrice());
        assertEquals(priceDTO.getPriceDate(), responseEntity.getBody().getPriceDate());
    }

    @Test
    public void testCreate_IdNotNull_ShouldReturnNewTableDTO_OK_200() {
        String url = "/api/drink-prices/create";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(9)
                .drinkId(1)
                .drinkCode("")
                .price(300.00)
                .priceDate(LocalDate.now())
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkPriceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, DrinkPriceDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals(priceDTO.getDrinkId(), responseEntity.getBody().getDrinkId());
        assertEquals(priceDTO.getPrice(), responseEntity.getBody().getPrice());
        assertEquals(priceDTO.getPriceDate(), responseEntity.getBody().getPriceDate());
    }

    @Test
    public void testCreate_IdExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/drink-prices/create";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(1)
                .drinkId(1)
                .drinkCode("")
                .price(300.00)
                .priceDate(LocalDate.now())
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkPriceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, priceDTOHttpEntity, DrinkPriceDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testOne_IdExisting_ShouldReturnTableDTO_OK_200() {
        String url = "/api/drink-prices/one/1";

        ResponseEntity<DrinkPriceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkPriceDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals(1, (int) responseEntity.getBody().getId());
        assertEquals(1, (int) responseEntity.getBody().getDrinkId());
        assertEquals((Double) 150.0, responseEntity.getBody().getPrice());
        assertEquals(LocalDate.of(2021, 12, 6), responseEntity.getBody().getPriceDate());
    }

    @Test
    public void testOne_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/drink-prices/one/44";

        ResponseEntity<DrinkPriceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkPriceDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAll_ShouldReturnList_OK_200() {
        String url = "/api/drink-prices/all";

        ResponseEntity<DrinkPriceDTO[]> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, DrinkPriceDTO[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertEquals(repository.findAll().size(), responseEntity.getBody().length);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChangedTableDTO_OK_200() {
        String url = "/api/drink-prices/edit/3";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(3)
                .drinkId(3)
                .drinkCode("")
                .price(300.00)
                .priceDate(LocalDate.now())
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkPriceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, DrinkPriceDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertNotEquals(3, (int) responseEntity.getBody().getId());
        assertEquals(3, (int) responseEntity.getBody().getDrinkId());
        assertEquals((Double) 300.0, responseEntity.getBody().getPrice());
        assertEquals(LocalDate.now(), responseEntity.getBody().getPriceDate());
    }

    @Test
    public void testEdit_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/drink-prices/edit/55";

        DrinkPriceDTO priceDTO = DrinkPriceDTO.builder()
                .id(55)
                .drinkId(3)
                .drinkCode("")
                .price(300.00)
                .priceDate(LocalDate.now())
                .build();
        HttpEntity<DrinkPriceDTO> priceDTOHttpEntity = new HttpEntity<>(priceDTO);
        ResponseEntity<DrinkPriceNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, priceDTOHttpEntity, DrinkPriceNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturnTrue_OK_200() {
        String url = "/api/drink-prices/delete/2";

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertTrue(responseEntity.getBody());
    }

    @Test
    public void testDelete_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/drink-prices/delete/55";

        ResponseEntity<DrinkPriceNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, DrinkPriceNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    //-------------------------------------
    public String json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(obj);
    }

}
