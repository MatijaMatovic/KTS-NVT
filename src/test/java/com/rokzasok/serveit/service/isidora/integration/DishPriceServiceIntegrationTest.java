package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.converters.DishPriceToDishPriceDTO;
import com.rokzasok.serveit.exceptions.DishNotFoundException;
import com.rokzasok.serveit.exceptions.DishPriceNotFoundException;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.repository.DishPriceRepository;
import com.rokzasok.serveit.service.impl.DishPriceService;
import com.rokzasok.serveit.service.impl.DishService;
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
public class DishPriceServiceIntegrationTest {

    @Autowired
    private DishPriceService dishPriceService;
    @Autowired
    private DishPriceRepository dishPriceRepository;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishPriceToDishPriceDTO dishPriceToDishPriceDTO;

    @Test
    public void testFindAll() {
        List<DishPrice> found = dishPriceService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS_DISH, found.size());
    }

    @Test
    public void testFindOne_IdExisting_ShouldReturn_Price() {
        DishPrice found = dishPriceService.findOne(ID1);
        assertEquals(ID1, found.getId());
        assertEquals((Double) 150.0, found.getPrice());
        assertEquals(LocalDate.of(2021, 12, 6), found.getPriceDate());
        assertEquals(1, (int) found.getDish().getId());
        assertFalse(found.getIsDeleted());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        DishPrice found = dishPriceService.findOne(NON_EXISTING_ID);
        assertNull(found);
    }

    // TODO unnecessary
    @Test
    public void testSave(){
        Dish d = dishService.findOne(DISH_ID);
        DishPrice dishPrice = DishPrice.builder()
                .id(null)
                .dish(d)
                .price(150.0)
                .priceDate(LocalDate.now())
                .isDeleted(false)
                .build();
        List<DishPrice> oldAll = dishPriceService.findAll();
        Integer lastOldId = oldAll.get(oldAll.size() - 1).getId();
        DishPrice created = dishPriceService.save(dishPrice);

        assertNotEquals(lastOldId, created.getId());
    }

    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        Boolean success = dishPriceService.deleteOne(ID_TO_DELETE);
        DishPrice dishPrice = dishPriceService.findOne(ID_TO_DELETE);
        assertNull(dishPrice);
        assertTrue(success);
    }

    @Test(expected = DishPriceNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        dishPriceService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_DishIdExisting_MakesNewPriceAndDeletesOld() throws Exception {
        DishPrice toEdit = dishPriceService.findOne(4);
        Double beforeEditPrice = toEdit.getPrice();
        toEdit.setPrice(beforeEditPrice + 10000.0);

        DishPrice edited = dishPriceService.edit(toEdit.getDish().getId(), dishPriceToDishPriceDTO.convert(toEdit));
        assertNotEquals(4, (int) edited.getId());
        assertNotEquals(beforeEditPrice, edited.getPrice());
        assertNull(dishPriceService.findOne(4));
    }

    @Test(expected = DishNotFoundException.class)
    public void testEdit_UsedIdNotExisting_ShouldThrow_DishNotFound() throws Exception {
        DishPrice toEdit = dishPriceService.findOne(ID1);
        Double beforeEditPrice = toEdit.getPrice();
        toEdit.setPrice(beforeEditPrice + 10000.0);

        dishPriceService.edit(NON_EXISTING_ID, dishPriceToDishPriceDTO.convert(toEdit));
    }

}
