package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkPriceRepository;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
import com.rokzasok.serveit.service.impl.DrinkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.rokzasok.serveit.constants.PriceConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DrinkPriceServiceIntegrationTest {

    @Autowired
    private DrinkPriceService drinkPriceService;
    @Autowired
    private DrinkPriceRepository drinkPriceRepository;
    @Autowired
    private DrinkService drinkService;

    @Test
    public void testFindAll() {
        List<DrinkPrice> found = drinkPriceService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS_DRINK, found.size());
    }

    @Test
    public void testFindById() {
        DrinkPrice found = drinkPriceService.findOne(ID1);
        assertEquals(ID1, found.getId());
    }

    @Test
    public void testSave(){
        Drink d = drinkService.findOne(DRINK_ID);
        DrinkPrice drinkPrice = DrinkPrice.builder()
                .id(null)
                .drink(d)
                .price(150.0)
                .priceDate(LocalDate.now())
                .isDeleted(false).build();
        List<DrinkPrice> oldAll = drinkPriceService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        DrinkPrice created = drinkPriceService.save(drinkPrice);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete() throws Exception {
        drinkPriceService.deleteOne(ID_TO_DELETE);
        DrinkPrice drinkPrice = drinkPriceService.findOne(ID_TO_DELETE);
        assertNull(drinkPrice);
    }

}
