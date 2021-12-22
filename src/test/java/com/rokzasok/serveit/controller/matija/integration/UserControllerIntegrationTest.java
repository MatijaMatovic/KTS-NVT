package com.rokzasok.serveit.controller.matija.integration;

import com.rokzasok.serveit.dto.JwtAuthenticationRequest;
import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.dto.UserTokenState;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.service.impl.UserService;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserControllerIntegrationTest {
    public static final String URL_PREFIX = "/api/users";
    public static final int DB_SIZE = 8;
    public static final Integer ADMIN_ID = 1;
    public static final Integer WAITER_ID = 8;
    public static final Integer NON_EXISTENT_USER_ID = 420;

    @Autowired
    TestRestTemplate dispatcher;

    @Autowired
    UserService userService;


    public String login(String username, String password) {
        ResponseEntity<UserTokenState> responseEntity
                = dispatcher.postForEntity(
                        "/auth/login",
                        new JwtAuthenticationRequest(username,password),
                        UserTokenState.class
        );
        return "Bearer " + responseEntity.getBody().getAccessToken();
    }


    @Test
    public void testGetAllUsers() {
        int initialDbSize = userService.findAll().size();

        ResponseEntity<UserDTO[]> foundUsersResponse
                = dispatcher.getForEntity(URL_PREFIX + "/all", UserDTO[].class);

        UserDTO[] foundUsers = foundUsersResponse.getBody();
        assertNotNull(foundUsers);
        assertTrue(foundUsers.length >= DB_SIZE-1);
        assertEquals(initialDbSize, foundUsers.length);
    }

    @Test
    public void testGetOne() {
        ResponseEntity<UserDTO> foundUserResponse
                = dispatcher.getForEntity(URL_PREFIX + "/one/" + ADMIN_ID, UserDTO.class);

        UserDTO foundUser = foundUserResponse.getBody();
        assertNotNull(foundUser);
        assertEquals(ADMIN_ID, foundUser.getId());
        assertEquals(UserType.ADMINISTRATOR, foundUser.getType());
    }

    @Test
    public void testGetOne_UserNotExists() {
        ResponseEntity<UserDTO> foundUserResponse
                = dispatcher.getForEntity(URL_PREFIX + "/one/" + NON_EXISTENT_USER_ID, UserDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, foundUserResponse.getStatusCode());
    }

    @Test
    public void testEdit() {
        User toEdit = userService.findOne(ADMIN_ID);
        UserDTO toEditDTO = new UserDTO(toEdit);

        String newName = "NovoIme";

        toEditDTO.setFirstName(newName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", login("admin", "password"));

        ResponseEntity<UserDTO> editedUserResponse
                = dispatcher.exchange(
                        URL_PREFIX + "/edit", HttpMethod.PUT,
                new HttpEntity<>(toEditDTO, headers), UserDTO.class
        );

        UserDTO editedUser = editedUserResponse.getBody();
        assertNotNull(editedUser);
        assertEquals(ADMIN_ID, editedUser.getId());
        assertEquals(newName, editedUser.getFirstName());
    }


    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteUser() {
        User foundUser = userService.findOne(WAITER_ID);
        int initialDbSize = userService.findAll().size();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", login("admin", "password"));

        ResponseEntity<Boolean> deletedResponse
                = dispatcher.exchange(
                        URL_PREFIX + "/delete/" + WAITER_ID,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, headers),
                        Boolean.class
                );

        Boolean success = deletedResponse.getBody();
        int dbSize = userService.findAll().size();
        assertNotNull(success);
        assertTrue(success);
        assertEquals(initialDbSize-1, dbSize);

        userService.save(foundUser); // Returning the database to initial state
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteUser_DeleteAdminFails() {
        int initialDbSize = userService.findAll().size();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", login("admin", "password"));

        ResponseEntity<Object> deletedResponse
                = dispatcher.exchange(
                URL_PREFIX + "/delete/" + ADMIN_ID,
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                Object.class
        );

        int dbSize = userService.findAll().size();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, deletedResponse.getStatusCode());
        assertEquals(initialDbSize, dbSize);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteUser_NonExistingUser() {
        int initialDbSize = userService.findAll().size();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", login("admin", "password"));

        ResponseEntity<Object> deletedResponse
                = dispatcher.exchange(
                URL_PREFIX + "/delete/" + NON_EXISTENT_USER_ID,
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                Object.class
        );

        int dbSize = userService.findAll().size();
        assertEquals(HttpStatus.NOT_FOUND, deletedResponse.getStatusCode());
        assertEquals(initialDbSize, dbSize);
    }
}
