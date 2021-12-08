package com.rokzasok.serveit.service.matija.unit;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserServiceUnitTest {

    private static final Integer USER_ID = 1;
    private static final Integer OTHER_USER_ID = 55;
    private static final Integer NON_EXISTING_ID = 420;

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    User user;
    User other;
    User empty;

    @PostConstruct
    public void setup() {
        user = new User();
        user.setUsername("User");
        user.setPassword("Password");
        user.setFirstName("Ime");
        user.setLastName("Prezime");
        user.setAddress("Adresa");
        user.setImagePath("Path");
        user.setEmail("mail@example.com");
        user.setType(UserType.WAITER);
        user.setPhoneNumber("123");
        user.setIsDeleted(false);

        other = new User();
        other.setId(OTHER_USER_ID);
        other.setUsername("User2");
        other.setPassword("Password2");
        other.setFirstName("Ime");
        other.setLastName("Prezime");
        other.setAddress("Adresa2");
        other.setImagePath("Path");
        other.setEmail("mail@example.com");
        other.setType(UserType.WAITER);
        other.setPhoneNumber("123");
        other.setIsDeleted(false);

        empty = new User();
        empty.setId(NON_EXISTING_ID);

        List<User> users = new ArrayList<>();
        users.add(user);

        // testFindAll
        given(userRepository.findAll()).willReturn(users);

        // testFindOne
        given(userRepository.findById(USER_ID)).willReturn(Optional.ofNullable(user));

        // testFindOne_NonExistingID
        Optional<User> userNull = Optional.empty(); //Da bi orElse mogao da vrati Null
        given(userRepository.findById(NON_EXISTING_ID)).willReturn(userNull);
        doNothing().when(userRepository).delete(user);

        // testEdit
        given(userRepository.findById(OTHER_USER_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.save(any())).willReturn(other);

    }


    @Test
    public void testFindAll() {
        List<User> foundUsers = userService.findAll();

        verify(userRepository, times(1)).findAll();
        assertEquals(1, foundUsers.size());
    }

    @Test
    public void testDelete() {
        Boolean deleted = userService.deleteOne(1);

        verify(userRepository, times(1)).findById(1);
        assertEquals(true, deleted);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDelete_NonExistingID() {
        Boolean deleted = userService.deleteOne(NON_EXISTING_ID);

        assertEquals(false, deleted);
    }

    @Test
    public void testEdit() {
        User editedUser = userService.edit(other);

        verify(userRepository, times(1)).findById(OTHER_USER_ID);
        assertEquals(editedUser.getFirstName(), other.getFirstName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEdit_NonExistingID() {
        User editedUser = userService.edit(empty);

        verify(userRepository, times(1)).findById(OTHER_USER_ID);
        assertEquals(editedUser.getUsername(), other.getUsername());
    }

}
