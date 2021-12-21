package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.repository.DishPriceRepository;
import com.rokzasok.serveit.service.impl.DishPriceService;
import com.rokzasok.serveit.service.impl.DishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.rokzasok.serveit.constants.PriceConstants.*;
import static com.rokzasok.serveit.constants.PriceConstants.ID_TO_DELETE;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DishPriceServiceIntegrationTest {

    @Autowired
    private DishPriceService dishPriceService;
    @Autowired
    private DishPriceRepository dishPriceRepository;
    @Autowired
    private DishService dishService;

    @Test
    public void testFindAll() {
        List<DishPrice> found = dishPriceService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS_DISH, found.size());
    }

    @Test
    public void testFindById() {
        DishPrice found = dishPriceService.findOne(ID1);
        assertEquals(ID1, found.getId());
    }

    @Test
    public void testSave(){
        Dish d = dishService.findOne(DISH_ID);
        DishPrice dishPrice = DishPrice.builder()
                .id(null)
                .dish(d)
                .price(150.0)
                .priceDate(LocalDate.now())
                .isDeleted(false).build();
        List<DishPrice> oldAll = dishPriceService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        DishPrice created = dishPriceService.save(dishPrice);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete() throws Exception {
        dishPriceService.deleteOne(ID_TO_DELETE);
        DishPrice dishPrice = dishPriceService.findOne(ID_TO_DELETE);
        assertNull(dishPrice);
    }

}
