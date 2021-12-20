package com.rokzasok.serveit.service.isidora.integration;

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

    @Test
    public void testFindAll() {
        List<UserSalary> found = userSalaryService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS, found.size());
    }

    @Test
    public void testFindById() {
        UserSalary found = userSalaryService.findOne(ID1);
        assertEquals(ID1, found.getId());
    }

    @Test
    public void testSave(){
        User user = userService.findOne(2);
        UserSalary userSalary = UserSalary.builder().id(null).user(user).salary(35000.00).salaryDate(LocalDate.now()).isDeleted(false).build();
        List<UserSalary> oldAll = userSalaryService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        UserSalary created = userSalaryService.save(userSalary);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete(){
        userSalaryService.deleteOne(ID_TO_DELETE);
        UserSalary userSalary = userSalaryService.findOne(ID_TO_DELETE);
        assertNull(userSalary);
    }

    @Test
    public void testCurrentSalaryForUser() {
        User user = userService.findOne(USER_ID_FOR_CURRENT);
        List<UserSalary> all = userSalaryRepository.findByUser(user);
        UserSalary curr = userSalaryService.current(user);
        assertTrue(all.get(0).getSalaryDate().isBefore(curr.getSalaryDate()));
        assertTrue(curr.getSalaryDate().isBefore(LocalDate.now()));
    }

    @Test
    public void testEdit_MakesNewSalaryAndDeletesOld() {
        UserSalary toEdit = userSalaryService.findOne(ID_TO_EDIT);
        Double beforeEditSalary = toEdit.getSalary();
        toEdit.setSalary(beforeEditSalary + 10000.0);
        UserSalary edited = userSalaryService.edit(toEdit);
        assertNotEquals(ID_TO_EDIT, edited.getId());
        assertNotEquals(beforeEditSalary, edited.getSalary());
        assertNull(userSalaryService.findOne(ID_TO_EDIT));
    }

}