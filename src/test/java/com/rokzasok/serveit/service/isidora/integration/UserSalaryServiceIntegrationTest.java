package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.converters.UserSalaryToUserSalaryDTO;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.exceptions.UserSalaryNotFoundException;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import com.rokzasok.serveit.service.impl.UserSalaryService;
import com.rokzasok.serveit.service.impl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.rokzasok.serveit.constants.TableConstants.ID_TO_DELETE;
import static com.rokzasok.serveit.constants.TableConstants.NON_EXISTING_ID;
import static com.rokzasok.serveit.constants.UserSalaryConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserSalaryServiceIntegrationTest {

    @Autowired
    private UserSalaryService userSalaryService;
    @Autowired
    private UserSalaryRepository userSalaryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSalaryToUserSalaryDTO userSalaryToUserSalaryDTO;

    @Test
    public void testFindAll_ShouldReturn_List() {
        List<UserSalary> found = userSalaryService.findAll();
        assertEquals(userSalaryRepository.findAll().size(), found.size());
    }

    @Test
    public void testFindOne_IdExisting_ShouldReturn_Salary() {
        UserSalary found = userSalaryService.findOne(ID1);
        assertEquals(ID1, found.getId());
        assertEquals(3, (int) found.getUser().getId());
        assertEquals((Double) 80000.0, found.getSalary());
        assertEquals(LocalDate.of(2021, 11, 6), found.getSalaryDate());
        assertFalse(found.getIsDeleted());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        UserSalary found = userSalaryService.findOne(NON_EXISTING_ID);
        assertNull(found);
    }

    // TODO unnecessary
    @Test
    public void testSave(){
        User u = userService.findOne(3);
        UserSalary userSalary = UserSalary.builder()
                .id(null)
                .user(u)
                .salary(80000.0)
                .salaryDate(LocalDate.now())
                .isDeleted(false)
                .build();
        List<UserSalary> oldAll = userSalaryService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        UserSalary created = userSalaryService.save(userSalary);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        Boolean success = userSalaryService.deleteOne(ID_TO_DELETE);
        UserSalary userSalary = userSalaryService.findOne(ID_TO_DELETE);
        assertNull(userSalary);
        assertTrue(success);
    }

    @Test(expected = UserSalaryNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        userSalaryService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_UsedIdExisting_MakesNewSalaryAndDeletesOld() throws Exception {
        UserSalary toEdit = userSalaryService.findOne(4);
        Double beforeEditSalary = toEdit.getSalary();
        toEdit.setSalary(beforeEditSalary + 10000.0);

        UserSalary edited = userSalaryService.edit(toEdit.getUser().getId(), userSalaryToUserSalaryDTO.convert(toEdit));
        assertNotEquals(4, (int) edited.getId());
        assertNotEquals(beforeEditSalary, edited.getSalary());
        assertNull(userSalaryService.findOne(4));
    }

    @Test(expected = UserNotFoundException.class)
    public void testEdit_UserIdNotExisting_ShouldThrow_UserNotFound() throws Exception {
        UserSalary toEdit = userSalaryService.findOne(ID1);
        Double beforeEditSalary = toEdit.getSalary();
        toEdit.setSalary(beforeEditSalary + 10000.0);

        userSalaryService.edit(NON_EXISTING_ID, userSalaryToUserSalaryDTO.convert(toEdit));
    }

    @Test
    public void testCurrent_UserIdExisting_ShouldReturnSalary() throws Exception {
        User user = userService.findOne(USER_ID_FOR_CURRENT);
        List<UserSalary> all = userSalaryRepository.findByUser(user);
        UserSalary curr = userSalaryService.current(user);
        assertTrue(all.get(0).getSalaryDate().isBefore(curr.getSalaryDate()));
        assertTrue(curr.getSalaryDate().isBefore(LocalDate.now()));
    }

    @Test(expected = UserSalaryNotFoundException.class)
    public void testCurrent_UserIdNotExisting_ShouldReturnNull() throws Exception {
        User user = userService.findOne(44);
        userSalaryService.current(user);
    }

}