package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.converters.DrinkPriceToDrinkPriceDTO;
import com.rokzasok.serveit.exceptions.DrinkNotFoundException;
import com.rokzasok.serveit.exceptions.DrinkPriceNotFoundException;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkPriceRepository;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
import com.rokzasok.serveit.service.impl.DrinkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.rokzasok.serveit.constants.PriceConstants.*;
import static com.rokzasok.serveit.constants.TableConstants.NON_EXISTING_ID;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DrinkPriceServiceIntegrationTest {

    @Autowired
    private DrinkPriceService drinkPriceService;
    @Autowired
    private DrinkPriceRepository drinkPriceRepository;
    @Autowired
    private DrinkService drinkService;
    @Autowired
    private DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO;

    @Test
    public void testFindAll() {
        List<DrinkPrice> found = drinkPriceService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS_DRINK, found.size());
    }

    @Test
    public void testFindOne_IdExisting_ShouldReturn_Price() {
        DrinkPrice found = drinkPriceService.findOne(ID1);
        assertEquals(ID1, found.getId());
        assertEquals((Double) 150.0, found.getPrice());
        assertEquals(LocalDate.of(2021, 12, 6), found.getPriceDate());
        assertEquals(1, (int) found.getDrink().getId());
        assertFalse(found.getIsDeleted());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        DrinkPrice found = drinkPriceService.findOne(NON_EXISTING_ID);
        assertNull(found);
    }

    // TODO unnecessary
    @Test
    public void testSave(){
        Drink d = drinkService.findOne(DRINK_ID);
        DrinkPrice drinkPrice = DrinkPrice.builder()
                .id(null)
                .drink(d)
                .price(150.0)
                .priceDate(LocalDate.now())
                .isDeleted(false)
                .build();
        List<DrinkPrice> oldAll = drinkPriceService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        DrinkPrice created = drinkPriceService.save(drinkPrice);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        Boolean success = drinkPriceService.deleteOne(ID_TO_DELETE);
        DrinkPrice drinkPrice = drinkPriceService.findOne(ID_TO_DELETE);
        assertNull(drinkPrice);
        assertTrue(success);
    }

    @Test(expected = DrinkPriceNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        drinkPriceService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_DrinkIdExisting_MakesNewPriceAndDeletesOld() throws Exception {
        DrinkPrice toEdit = drinkPriceService.findOne(4);
        Double beforeEditPrice = toEdit.getPrice();
        toEdit.setPrice(beforeEditPrice + 10000.0);

        DrinkPrice edited = drinkPriceService.edit(toEdit.getDrink().getId(), drinkPriceToDrinkPriceDTO.convert(toEdit));
        assertNotEquals(4, (int) edited.getId());
        assertNotEquals(beforeEditPrice, edited.getPrice());
        assertNull(drinkPriceService.findOne(4));
    }

    @Test(expected = DrinkNotFoundException.class)
    public void testEdit_UsedIdNotExisting_ShouldThrow_DrinkNotFound() throws Exception {
        DrinkPrice toEdit = drinkPriceService.findOne(ID1);
        Double beforeEditPrice = toEdit.getPrice();
        toEdit.setPrice(beforeEditPrice + 10000.0);

        drinkPriceService.edit(NON_EXISTING_ID, drinkPriceToDrinkPriceDTO.convert(toEdit));
    }

}
