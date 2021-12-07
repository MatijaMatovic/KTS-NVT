package com.rokzasok.serveit.repository.isidora;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.rokzasok.serveit.constatns.UserSalaryConstants.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserSalaryRepositoryIntegrationTest {

    @Autowired
    private UserSalaryRepository userSalaryRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user = userRepository.findById(1).orElse(null);
        UserSalary salary1 = UserSalary.builder().id(ID1).salaryDate(DATE1).isDeleted(IS_DELETED1).user(user).salary(30000.00).build();
        userSalaryRepository.save(salary1);

        UserSalary salary2 = UserSalary.builder().id(ID2).salaryDate(DATE2).isDeleted(IS_DELETED2).user(user).salary(40000.00).build();
        userSalaryRepository.save(salary2);
    }

    @Test
    public void testFindByUser() {
        User user = userRepository.findById(1).orElse(null);
        assert user != null;
        List<UserSalary> found = userSalaryRepository.findByUser(user);
        for (UserSalary salary : found){
            assertEquals(salary.getUser().getId(), user.getId());
        }
    }
}
