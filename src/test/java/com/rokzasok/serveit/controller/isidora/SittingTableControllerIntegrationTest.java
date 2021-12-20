package com.rokzasok.serveit.controller.isidora;

import com.rokzasok.serveit.dto.SittingTableDTO;
import com.rokzasok.serveit.exceptions.SittingTableNotFoundException;
import com.rokzasok.serveit.service.impl.SittingTableService;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SittingTableControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SittingTableService service;

    @Test
    public void testCreate_IdNull_ShouldReturnNewTableDTO_OK_200() {
        String url = "/api/sitting-tables/create";

        SittingTableDTO tableDTO = SittingTableDTO.builder().id(null).name("sto1").x(1).y(2).build();
        HttpEntity<SittingTableDTO> tableDTOHttpEntity = new HttpEntity<>(tableDTO);
        ResponseEntity<SittingTableDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, tableDTOHttpEntity, SittingTableDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 5, responseEntity.getBody().getId());
        assertEquals(tableDTO.getName(), responseEntity.getBody().getName());
        assertEquals(tableDTO.getX(), responseEntity.getBody().getX());
        assertEquals(tableDTO.getY(), responseEntity.getBody().getY());
    }

    @Test
    public void testCreate_IdNotNull_ShouldReturnNewTableDTO_OK_200() {
        String url = "/api/sitting-tables/create";

        SittingTableDTO tableDTO = SittingTableDTO.builder().id(6).name("sto5").x(1).y(2).build();
        HttpEntity<SittingTableDTO> tableDTOHttpEntity = new HttpEntity<>(tableDTO);
        ResponseEntity<SittingTableDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, tableDTOHttpEntity, SittingTableDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals(tableDTO.getId(), responseEntity.getBody().getId());
        assertEquals(tableDTO.getName(), responseEntity.getBody().getName());
        assertEquals(tableDTO.getX(), responseEntity.getBody().getX());
        assertEquals(tableDTO.getY(), responseEntity.getBody().getY());
    }

    @Test
    public void testCreate_IdExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/sitting-tables/create";

        SittingTableDTO tableDTO = SittingTableDTO.builder().id(1).name("sto").x(1).y(2).build();
        HttpEntity<SittingTableDTO> tableDTOHttpEntity = new HttpEntity<>(tableDTO);
        ResponseEntity<SittingTableDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, tableDTOHttpEntity, SittingTableDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testOne_IdExisting_ShouldReturnTableDTO_OK_200() {
        String url = "/api/sitting-tables/one/1";

        ResponseEntity<SittingTableDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, SittingTableDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 1, responseEntity.getBody().getId());
        assertEquals("Sto 1", responseEntity.getBody().getName());
        assertEquals((Integer) 1, responseEntity.getBody().getX());
        assertEquals((Integer) 1, responseEntity.getBody().getY());
    }

    @Test
    public void testOne_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/sitting-tables/one/44";

        ResponseEntity<SittingTableDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, SittingTableDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAll_ShouldReturnList_OK_200() {
        String url = "/api/sitting-tables/all";

        ResponseEntity<SittingTableDTO[]> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, SittingTableDTO[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertEquals(5, responseEntity.getBody().length);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChangedTableDTO_OK_200() {
        String url = "/api/sitting-tables/edit/3";

        SittingTableDTO tableDTO = SittingTableDTO.builder().id(3).name("sto").x(2).y(2).build();
        HttpEntity<SittingTableDTO> tableDTOHttpEntity = new HttpEntity<>(tableDTO);
        ResponseEntity<SittingTableDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, tableDTOHttpEntity, SittingTableDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals((Integer) 3, responseEntity.getBody().getId());
        assertEquals(tableDTO.getName(), responseEntity.getBody().getName());
        assertEquals(tableDTO.getX(), responseEntity.getBody().getX());
        assertEquals(tableDTO.getY(), responseEntity.getBody().getY());
    }

    @Test
    public void testEdit_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/sitting-tables/edit/55";

        SittingTableDTO tableDTO = SittingTableDTO.builder().id(55).name("sto").x(1).y(2).build();
        HttpEntity<SittingTableDTO> tableDTOHttpEntity = new HttpEntity<>(tableDTO);
        ResponseEntity<SittingTableNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, tableDTOHttpEntity, SittingTableNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturnTrue_OK_200() {
        String url = "/api/sitting-tables/delete/2";

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertTrue(responseEntity.getBody());
    }

    @Test
    public void testDelete_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/sitting-tables/delete/55";

        ResponseEntity<SittingTableNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, SittingTableNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
