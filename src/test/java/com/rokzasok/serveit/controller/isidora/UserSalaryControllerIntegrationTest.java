package com.rokzasok.serveit.controller.isidora;

import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.exceptions.UserSalaryNotFoundException;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import com.rokzasok.serveit.service.impl.UserSalaryService;
import org.junit.Test;
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

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserSalaryControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserSalaryService service;

    @Autowired
    private UserSalaryRepository repository;

    @Test
    public void testCreate_IdNull_ShouldReturnNewTableDTO_OK_200() {
        String url = "/api/user-salaries/create";

        UserSalaryDTO salaryDTO = UserSalaryDTO.builder().id(null).userId(1).salaryDate(LocalDate.now()).salary(30000.0).build();
        HttpEntity<UserSalaryDTO> salaryDTOHttpEntity = new HttpEntity<>(salaryDTO);
        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, salaryDTOHttpEntity, UserSalaryDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals(salaryDTO.getUserId(), responseEntity.getBody().getUserId());
        assertEquals(salaryDTO.getSalaryDate(), responseEntity.getBody().getSalaryDate());
        assertEquals(salaryDTO.getSalary(), responseEntity.getBody().getSalary());
    }

    @Test
    public void testCreate_IdNotNull_ShouldReturnNewTableDTO_OK_200() {
        String url = "/api/user-salaries/create";

        UserSalaryDTO salaryDTO = UserSalaryDTO.builder().id(44).userId(1).salaryDate(LocalDate.now()).salary(31000.0).build();
        HttpEntity<UserSalaryDTO> salaryDTOHttpEntity = new HttpEntity<>(salaryDTO);
        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, salaryDTOHttpEntity, UserSalaryDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals(salaryDTO.getUserId(), responseEntity.getBody().getUserId());
        assertEquals(salaryDTO.getSalaryDate(), responseEntity.getBody().getSalaryDate());
        assertEquals(salaryDTO.getSalary(), responseEntity.getBody().getSalary());
    }

    @Test
    public void testCreate_IdExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/user-salaries/create";

        UserSalaryDTO salaryDTO = UserSalaryDTO.builder().id(1).userId(1).salaryDate(LocalDate.now()).salary(32000.0).build();
        HttpEntity<UserSalaryDTO> salaryDTOHttpEntity = new HttpEntity<>(salaryDTO);
        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, salaryDTOHttpEntity, UserSalaryDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCreate_IdNotNull_UserIdNotExisting_ShouldThrow_BadRequest_NotFound_404() {
        String url = "/api/user-salaries/create";

        UserSalaryDTO salaryDTO = UserSalaryDTO.builder().id(6).userId(440).salaryDate(LocalDate.now()).salary(31000.0).build();
        HttpEntity<UserSalaryDTO> salaryDTOHttpEntity = new HttpEntity<>(salaryDTO);
        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, salaryDTOHttpEntity, UserSalaryDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testOne_IdExisting_ShouldReturnTableDTO_OK_200() {
        String url = "/api/user-salaries/one/1";

        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, UserSalaryDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertEquals(1, (int) responseEntity.getBody().getId());
        assertEquals(3, (int) responseEntity.getBody().getUserId());
        assertEquals(LocalDate.of(2021, 11, 6), responseEntity.getBody().getSalaryDate());
        assertEquals((Double) 80000.00, responseEntity.getBody().getSalary());
    }

    @Test
    public void testOne_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/user-salaries/one/44";

        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, UserSalaryDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    // TODO unnecessary
    @Test
    public void testAll_ShouldReturnList_OK_200() {
        String url = "/api/user-salaries/all";

        ResponseEntity<UserSalaryDTO[]> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, UserSalaryDTO[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        int size = repository.findAll().size();

        assertEquals(size, responseEntity.getBody().length);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChangedTableDTO_OK_200() {
        String url = "/api/user-salaries/edit/2";

        LocalDate date = LocalDate.now();

        UserSalaryDTO salaryDTO = UserSalaryDTO.builder().id(3).userId(2).salaryDate(date).salary(315000.0).build();
        HttpEntity<UserSalaryDTO> salaryDTOHttpEntity = new HttpEntity<>(salaryDTO);
        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, salaryDTOHttpEntity, UserSalaryDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertNotNull(responseEntity.getBody().getId());
        assertNotEquals((Integer) 3, responseEntity.getBody().getId());
        assertEquals(2, (int) responseEntity.getBody().getUserId());
        assertEquals(date, responseEntity.getBody().getSalaryDate());
        assertEquals((Double) 315000.0, responseEntity.getBody().getSalary());
    }

    @Test
    public void testEdit_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/user-salaries/edit/2";

        LocalDate date = LocalDate.now();

        UserSalaryDTO salaryDTO = UserSalaryDTO.builder().id(55).userId(2).salaryDate(date).salary(315000.0).build();
        HttpEntity<UserSalaryDTO> salaryDTOHttpEntity = new HttpEntity<>(salaryDTO);
        ResponseEntity<UserSalaryNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, salaryDTOHttpEntity, UserSalaryNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturnTrue_OK_200() {
        String url = "/api/user-salaries/delete/8";

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Boolean.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertTrue(responseEntity.getBody());
    }

    @Test
    public void testDelete_IdNotExisting_ShouldThrow_NotFound_404() {
        String url = "/api/user-salaries/delete/55";

        ResponseEntity<UserSalaryNotFoundException> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, UserSalaryNotFoundException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testCurrent_UserIdExisting_ShouldReturn_Salary_OK_200() {
        String url = "/api/user-salaries/current/3";

        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, UserSalaryDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertEquals(3, (int) responseEntity.getBody().getUserId());
        assertEquals(LocalDate.of( 2021, 12, 6), responseEntity.getBody().getSalaryDate());
        assertEquals((Double) 80000.00, responseEntity.getBody().getSalary());
    }

    @Test
    public void testCurrent_UserIdNotExisting_ShouldThrow_BadRequest_400() {
        String url = "/api/user-salaries/current/44";

        ResponseEntity<UserSalaryDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, UserSalaryDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCurrentAll_ShouldReturn_List_OK_200() {
        String url = "/api/user-salaries/current-salaries";

        ResponseEntity<UserSalaryDTO[]> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, UserSalaryDTO[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        assertEquals(7, responseEntity.getBody().length);
    }

}
