package com.rokzasok.serveit.repository.isidora;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.rokzasok.serveit.constants.UserSalaryConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserSalaryRepositoryIntegrationTest {

    @Autowired
    private UserSalaryRepository userSalaryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUser_UserExisting_ShouldReturnList() {
        User user = userRepository.findById(1).orElse(null);
        UserSalary salary1 = UserSalary.builder().id(USER_ID1).salaryDate(DATE1).isDeleted(IS_DELETED1).user(user).salary(30000.00).build();
        userSalaryRepository.save(salary1);

        UserSalary salary2 = UserSalary.builder().id(USER_ID2).salaryDate(DATE2).isDeleted(IS_DELETED2).user(user).salary(40000.00).build();
        userSalaryRepository.save(salary2);

        User userTest = userRepository.findById(1).orElse(null);
        assertNotNull(userTest);
        List<UserSalary> found = userSalaryRepository.findByUser(userTest);
        for (UserSalary salary : found){
            assertEquals(salary.getUser().getId(), userTest.getId());
        }
    }

    @Test
    public void testFindByUser_UserNotExisting_ShouldReturnEmptyList() {
        User userTest = userRepository.findById(44).orElse(null);
        assertNull(userTest);
        List<UserSalary> found = userSalaryRepository.findByUser(userTest);
        assertEquals(0, found.size());
    }
}
