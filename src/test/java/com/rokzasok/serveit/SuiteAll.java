package com.rokzasok.serveit;

import com.rokzasok.serveit.repository.isidora.DrinkMenuRepositoryIntegrationTest;
import com.rokzasok.serveit.repository.isidora.FoodMenuRepositoryIntegrationTest;
import com.rokzasok.serveit.repository.isidora.UserSalaryRepositoryIntegrationTest;
import com.rokzasok.serveit.service.isidora.DishPriceServiceUnitTest;
import com.rokzasok.serveit.service.isidora.DrinkPriceServiceUnitTest;
import com.rokzasok.serveit.service.isidora.UserSalaryServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
@SuiteClasses({FoodMenuRepositoryIntegrationTest.class,
        DrinkMenuRepositoryIntegrationTest.class,
        UserSalaryRepositoryIntegrationTest.class,
        UserSalaryServiceUnitTest.class,
        DishPriceServiceUnitTest.class,
        DrinkPriceServiceUnitTest.class})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {

}
