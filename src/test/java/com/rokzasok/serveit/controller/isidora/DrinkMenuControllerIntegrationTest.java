package com.rokzasok.serveit.controller.isidora;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.exceptions.DrinkMenuNotFoundException;
import com.rokzasok.serveit.repository.DrinkMenuRepository;
import com.rokzasok.serveit.service.impl.DrinkMenuService;
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
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
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
        assertEquals((Integer) 4, responseEntity.getBody().getId());
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
        String url = "/api/drink-menu/edit/3";

        DrinkMenuDTO menuDTO = DrinkMenuDTO.builder()
                .id(3)
                .date(LocalDate.now())
                .drinks(new ArrayList<>())
                .build();
        HttpEntity<DrinkMenuDTO> menuDTOHttpEntity = new HttpEntity<>(menuDTO);
        ResponseEntity<DrinkMenuDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, menuDTOHttpEntity, DrinkMenuDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 3, responseEntity.getBody().getId());
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
    // TODO: last, addDrink, copyCreate, deleteDrink, editDrinkPrice - testirati
    //-------------------------------------
    public String json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(obj);
    }

}
