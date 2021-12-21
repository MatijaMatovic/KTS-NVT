package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.exceptions.UserSalaryNotFoundException;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import com.rokzasok.serveit.service.impl.UserSalaryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rokzasok.serveit.constants.UserSalaryConstants.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class UserSalaryServiceUnitTest {

    @Autowired
    UserSalaryService userSalaryService;

    @MockBean
    UserSalaryRepository userSalaryRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    public void testFindOne_IdExisting_ShouldReturn_Table() {
        User user = User.builder()
                .id(1)
                .address("")
                .email("")
                .username("")
                .firstName("")
                .lastName("")
                .imagePath("")
                .password("")
                .isDeleted(false)
                .phoneNumber("")
                .type(UserType.WAITER)
                .enabled(true)
                .build();
        UserSalary salary = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();

        when(userSalaryRepository.findById(ID1)).thenReturn(Optional.ofNullable(salary));

        UserSalary found = userSalaryService.findOne(ID1);

        assertNotNull(found);
        assertEquals(salary.getId(), found.getId());
        assertEquals(salary.getUser().getId(), found.getUser().getId());
        assertEquals(salary.getSalaryDate(), found.getSalaryDate());
        assertEquals(salary.getSalary(), found.getSalary());
        assertFalse(found.getIsDeleted());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        when(userSalaryRepository.findById(ID1)).thenReturn(Optional.empty());

        UserSalary found = userSalaryService.findOne(ID1);

        assertNull(found);
    }

    // TODO maybe unnecessary everywhere
    @Test
    public void testFindAll_ShouldReturn_List() {
        User user = User.builder()
                .id(1)
                .address("")
                .email("")
                .username("")
                .firstName("")
                .lastName("")
                .imagePath("")
                .password("")
                .isDeleted(false)
                .phoneNumber("")
                .type(UserType.WAITER)
                .enabled(true)
                .build();
        UserSalary salary1 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();
        UserSalary salary2 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();

        List<UserSalary> salaries = new ArrayList<>();
        salaries.add(salary1);
        salaries.add(salary2);

        when(userSalaryRepository.findAll()).thenReturn(salaries);

        List<UserSalary> found = userSalaryService.findAll();

        verify(userSalaryRepository, times(1)).findAll();
        assertEquals(2, found.size());
    }

    // TODO unnecessary, ima smisla samo kao integracioni test
    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        User user = User.builder()
                .id(1)
                .address("")
                .email("")
                .username("")
                .firstName("")
                .lastName("")
                .imagePath("")
                .password("")
                .isDeleted(false)
                .phoneNumber("")
                .type(UserType.WAITER)
                .enabled(true)
                .build();
        UserSalary salary1 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();

        when(userSalaryRepository.findById(ID1)).thenReturn(Optional.ofNullable(salary1));

        Boolean deleted = userSalaryService.deleteOne(ID1);

        assertEquals(true, deleted);
    }

    @Test(expected = UserSalaryNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        when(userSalaryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        userSalaryService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChanged() throws Exception {
        User user = User.builder()
                .id(1)
                .address("")
                .email("")
                .username("")
                .firstName("")
                .lastName("")
                .imagePath("")
                .password("")
                .isDeleted(false)
                .phoneNumber("")
                .type(UserType.WAITER)
                .enabled(true)
                .build();
        UserSalary salary1 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();

        when(userSalaryRepository.findById(ID1)).thenReturn(Optional.ofNullable(salary1));

        UserSalaryDTO salary2ChangedDTO = UserSalaryDTO.builder()
                .id(ID1)
                .userId(user.getId())
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .build();

        UserSalary salary2Changed = UserSalary.builder()
                .id(ID2)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(false)
                .build();

        when(userSalaryRepository.save(any(UserSalary.class))).thenReturn(salary2Changed);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserSalary editedUserSalary = userSalaryService.edit(ID1, salary2ChangedDTO);

        verify(userSalaryRepository, times(1)).findById(ID1);
        assertNotEquals(salary1.getId(), editedUserSalary.getId());
        assertEquals(salary2Changed.getId(), editedUserSalary.getId());
    }

    @Test(expected = UserSalaryNotFoundException.class)
    public void testEdit_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        User user = User.builder()
                .id(1)
                .address("")
                .email("")
                .username("")
                .firstName("")
                .lastName("")
                .imagePath("")
                .password("")
                .isDeleted(false)
                .phoneNumber("")
                .type(UserType.WAITER)
                .enabled(true)
                .build();
        when(userSalaryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(userRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.ofNullable(user));

        UserSalaryDTO empty = UserSalaryDTO.builder()
                .id(NON_EXISTING_ID)
                .userId(NON_EXISTING_ID)
                .build();

        userSalaryService.edit(NON_EXISTING_ID, empty);
    }

    @Test(expected = UserNotFoundException.class)
    public void testEdit_UserIdNotExisting_ShouldThrow_UserNotFound() throws Exception {
        User user = User.builder()
                .id(1)
                .address("")
                .email("")
                .username("")
                .firstName("")
                .lastName("")
                .imagePath("")
                .password("")
                .isDeleted(false)
                .phoneNumber("")
                .type(UserType.WAITER)
                .enabled(true)
                .build();
        UserSalary salary1 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();
        when(userSalaryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.ofNullable(salary1));
        when(userRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        UserSalaryDTO empty = UserSalaryDTO.builder()
                .id(NON_EXISTING_ID)
                .userId(NON_EXISTING_ID)
                .build();

        userSalaryService.edit(NON_EXISTING_ID, empty);
    }

    @Test
    public void testCurrent_UserIdExisting_ShouldReturnSalary() throws Exception {
        User user = User.builder()
                .id(1)
                .address("")
                .email("")
                .username("")
                .firstName("")
                .lastName("")
                .imagePath("")
                .password("")
                .isDeleted(false)
                .phoneNumber("")
                .type(UserType.WAITER)
                .enabled(true)
                .build();
        UserSalary salary1 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now().minusMonths(3))
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();

        UserSalary salary2 = UserSalary.builder()
                .id(ID1)
                .user(user)
                .salaryDate(LocalDate.now())
                .salary(35000.0)
                .isDeleted(IS_DELETED1)
                .build();

        List<UserSalary> salaries = new ArrayList<>();
        salaries.add(salary1);
        salaries.add(salary2);

        when(userSalaryRepository.findByUser(user)).thenReturn(salaries);

        List<UserSalary> all = userSalaryRepository.findByUser(user);
        UserSalary curr = userSalaryService.current(user);
        assertTrue(all.get(0).getSalaryDate().isBefore(curr.getSalaryDate()));
        assertFalse(curr.getSalaryDate().isAfter(LocalDate.now()));
    }

    @Test(expected = UserSalaryNotFoundException.class)
    public void testCurrent_UserIdNotExisting_ShouldReturnNull() throws Exception {
        userSalaryService.current(null);
    }

}