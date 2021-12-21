package com.rokzasok.serveit;

import com.rokzasok.serveit.controller.isidora.SittingTableControllerIntegrationTest;
import com.rokzasok.serveit.controller.isidora.UserSalaryControllerIntegrationTest;
import com.rokzasok.serveit.repository.isidora.UserSalaryRepositoryIntegrationTest;
import com.rokzasok.serveit.service.isidora.integration.SittingTableServiceIntegrationTest;
import com.rokzasok.serveit.service.isidora.integration.UserSalaryServiceIntegrationTest;
import com.rokzasok.serveit.service.isidora.unit.SittingTableServiceUnitTest;
import com.rokzasok.serveit.service.isidora.unit.UserSalaryServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//        FoodMenuRepositoryIntegrationTest.class,
//        DrinkMenuRepositoryIntegrationTest.class,
//        UserSalaryRepositoryIntegrationTest.class,
//        UserSalaryServiceUnitTest.class,
//        DishPriceServiceUnitTest.class,
//        DrinkPriceServiceUnitTest.class,
//        FoodMenuServiceUnitTest.class,
//        DrinkMenuServiceUnitTest.class,
SittingTableServiceUnitTest.class, SittingTableServiceIntegrationTest.class, SittingTableControllerIntegrationTest.class,
UserSalaryRepositoryIntegrationTest.class, UserSalaryServiceUnitTest.class, UserSalaryServiceIntegrationTest.class, UserSalaryControllerIntegrationTest.class
//        ,
//        UserSalaryServiceIntegrationTest.class,
//        DrinkPriceServiceIntegrationTest.class,
//        DishPriceServiceIntegrationTest.class,
//        FoodMenuServiceIntegrationTest.class,
//        DrinkMenuServiceIntegrationTest.class
})
@TestPropertySource("classpath:test.properties")
public class SuiteIsidora {

}
