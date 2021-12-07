package com.rokzasok.serveit;

import com.rokzasok.serveit.repository.isidora.DrinkMenuRepositoryIntegrationTest;
import com.rokzasok.serveit.repository.isidora.FoodMenuRepositoryIntegrationTest;
import com.rokzasok.serveit.repository.isidora.UserSalaryRepositoryIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
@SuiteClasses({FoodMenuRepositoryIntegrationTest.class, DrinkMenuRepositoryIntegrationTest.class, UserSalaryRepositoryIntegrationTest.class})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {

}
