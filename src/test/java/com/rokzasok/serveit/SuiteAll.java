package com.rokzasok.serveit;

import com.rokzasok.serveit.repository.DrinkMenuRepositoryUnitTest;
import com.rokzasok.serveit.repository.FoodMenuRepositoryUnitTest;
import com.rokzasok.serveit.repository.UserSalaryRepositoryUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
@SuiteClasses({FoodMenuRepositoryUnitTest.class, DrinkMenuRepositoryUnitTest.class, UserSalaryRepositoryUnitTest.class})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {


}
