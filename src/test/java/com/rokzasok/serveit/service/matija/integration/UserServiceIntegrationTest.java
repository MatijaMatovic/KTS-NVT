package com.rokzasok.serveit.service.matija.integration;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserServiceIntegrationTest {
    public static final int DB_USER_COUNT = 9;
    public static final Integer USER_ID = 1;
    public static final Integer OTHER_USER_ID = 8;
    public static final Integer NON_EXISTENT_USER_ID = 420;

    @Autowired
    UserService userService;

    @Test
    public void testFindAll() {
        List<User> users = userService.findAll();

        assertEquals(DB_USER_COUNT, users.size());
    }

    @Test
    public void testFindOne() {
        User foundUser = userService.findOne(USER_ID);
        assertNotNull(foundUser);
        assertEquals(USER_ID, foundUser.getId());
    }

    @Test
    public void testFindOne_NonExistingID() {
        User foundUser = userService.findOne(NON_EXISTENT_USER_ID);
        assertNull(foundUser);
    }

    @Test
    public void testDeleteOne() {
        Boolean success = userService.deleteOne(OTHER_USER_ID);
        assertTrue(success);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteOne_NonExistingID() {
        Boolean success = userService.deleteOne(OTHER_USER_ID);
        fail("You shouldn't be here"); // Previous line should have thrown an exception
    }

    @Test
    public void testEdit() {
        User foundUser = userService.findOne(USER_ID);

        String newFirstName = "Test edited first name";
        String newLastName = "Test edited last name";
        foundUser.setFirstName(newFirstName);
        foundUser.setLastName(newLastName);

        User editedUser = userService.edit(foundUser);

        assertEquals(newFirstName, editedUser.getFirstName());
        assertEquals(newLastName, editedUser.getLastName());
    }
}
