package com.rokzasok.serveit;

import com.rokzasok.serveit.repository.isidora.DrinkMenuRepositoryIntegrationTest;
import com.rokzasok.serveit.repository.isidora.FoodMenuRepositoryIntegrationTest;
import com.rokzasok.serveit.repository.isidora.UserSalaryRepositoryIntegrationTest;
import com.rokzasok.serveit.service.isidora.integration.*;
import com.rokzasok.serveit.service.isidora.unit.*;
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
        DrinkPriceServiceUnitTest.class,
        FoodMenuServiceUnitTest.class,
        DrinkMenuServiceUnitTest.class,
        SittingTableServiceUnitTest.class,
        UserSalaryServiceIntegrationTest.class,
        DrinkPriceServiceIntegrationTest.class,
        DishPriceServiceIntegrationTest.class,
        FoodMenuServiceIntegrationTest.class,
        DrinkMenuServiceIntegrationTest.class})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {

}
