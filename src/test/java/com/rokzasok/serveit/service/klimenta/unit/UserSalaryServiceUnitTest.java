package com.rokzasok.serveit.service.klimenta.unit;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import com.rokzasok.serveit.service.impl.UserSalaryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rokzasok.serveit.constants.UserSalaryConstants.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class UserSalaryServiceUnitTest {

    @Autowired
    UserSalaryService userSalaryService;

    @MockBean
    UserSalaryRepository userSalaryRepository;

    User user;
    User userNoSalary;
    UserSalary salary1;
    UserSalary salary2;
    UserSalary salary3;
    UserSalary empty;

    @PostConstruct
    public void setup() {
        user = User.builder()
                .username("User")
                .password("Password")
                .firstName("Ime")
                .lastName("Prezime")
                .address("Adresa")
                .imagePath("Path")
                .email("mail@example.com")
                .type(UserType.WAITER)
                .phoneNumber("123")
                .isDeleted(false)
                .id(USER_ID1)
                .build();

        userNoSalary = User.builder()
                .username("User no")
                .password("Password")
                .firstName("Ime no")
                .lastName("Prezime no")
                .address("Adresa no")
                .imagePath("Path no")
                .email("mail@example.com no")
                .type(UserType.WAITER)
                .phoneNumber("123 no")
                .isDeleted(false)
                .id(USER_ID2)
                .build();

        salary1 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salary(SALARY1)
                .salaryDate(DATE1)
                .isDeleted(IS_DELETED1)
                .build();

        salary2 = UserSalary.builder()
                .id(ID2)
                .user(user)
                .salary(SALARY2)
                .salaryDate(DATE2)
                .isDeleted(IS_DELETED2)
                .build();

        salary3 = UserSalary.builder()
                .id(ID3)
                .user(user)
                .salary(SALARY3)
                .salaryDate(DATE3)
                .isDeleted(IS_DELETED3)
                .build();

        empty = UserSalary.builder()
                .id(NON_EXISTING_ID)
                .build();

        List<UserSalary> salaries = new ArrayList<>();
        salaries.add(salary1);
        salaries.add(salary2);

        // testFindAll
        given(userSalaryRepository.findAll()).willReturn(salaries);

        // testFindOne
        given(userSalaryRepository.findById(ID1)).willReturn(Optional.ofNullable(salary1));
        // testFindOne
        given(userSalaryRepository.findById(ID2)).willReturn(Optional.ofNullable(salary2));

        // testFindOne_NonExistingID
        Optional<UserSalary> userSalaryNull = Optional.empty(); //Da bi orElse mogao da vrati Null
        given(userSalaryRepository.findById(NON_EXISTING_ID)).willReturn(userSalaryNull);
        doNothing().when(userSalaryRepository).delete(salary1);

        // testEdit
        given(userSalaryRepository.findById(ID2)).willReturn(Optional.ofNullable(salary2));
        given(userSalaryRepository.save(any())).willReturn(salary3);
        //salaries.add(salary3);

        // testCurrent
        List<UserSalary> salaries2 = new ArrayList<>(salaries);
        salaries2.add(salary3);
        given(userSalaryRepository.findByUser(user)).willReturn(salaries2);

        // testCurrent_UserDoesNotHaveSalary
        given(userSalaryRepository.findByUser(userNoSalary)).willReturn(new ArrayList<>());

    }


    @Test
    public void testFindAll() {
        List<UserSalary> foundUserSalaries = userSalaryService.findAll();

        verify(userSalaryRepository, times(1)).findAll();
        assertEquals(2, foundUserSalaries.size());
    }

    @Test
    public void testDelete() {
        Boolean deleted = userSalaryService.deleteOne(ID1);

        verify(userSalaryRepository, times(1)).findById(ID1);
        assertEquals(true, deleted);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDelete_NonExistingID() {
        Boolean deleted = userSalaryService.deleteOne(NON_EXISTING_ID);

        assertEquals(false, deleted);
    }

    @Test
    public void testEdit() {
        UserSalary editedUserSalary = userSalaryService.edit(salary2);

        verify(userSalaryRepository, times(1)).findById(ID2);
        assertNotEquals(editedUserSalary.getId(), salary2.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEdit_NonExistingID() {
        UserSalary editedUserSalary = userSalaryService.edit(empty);

        verify(userSalaryRepository, times(1)).findById(ID2);
        assertEquals(editedUserSalary.getId(), salary2.getId());
    }

    @Test
    public void testCurrent() {
        UserSalary salary = userSalaryService.current(user);
        assertEquals("Current salary cannot be in the future, it must be in present", ID2, salary.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCurrent_UserDoesNotHaveSalary() {
        UserSalary salary = userSalaryService.current(userNoSalary);

        assertNull("Current salary should not exist", salary);
    }

}
