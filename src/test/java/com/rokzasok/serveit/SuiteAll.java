package com.rokzasok.serveit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
//@Suite.SuiteClasses({FoodMenuRepositoryIntegrationTest.class,
//        DrinkMenuRepositoryIntegrationTest.class,
//        UserSalaryRepositoryIntegrationTest.class,
//        UserSalaryServiceUnitTest.class,
//        DishPriceServiceUnitTest.class,
//        DrinkPriceServiceUnitTest.class,
//        FoodMenuServiceUnitTest.class,
//        DrinkMenuServiceUnitTest.class,
//        SittingTableServiceUnitTest.class,
//        UserSalaryServiceIntegrationTest.class,
//        DrinkPriceServiceIntegrationTest.class,
//        DishPriceServiceIntegrationTest.class,
//        FoodMenuServiceIntegrationTest.class,
//        DrinkMenuServiceIntegrationTest.class})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {

}
