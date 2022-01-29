package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.exceptions.DrinkNotFoundException;
import com.rokzasok.serveit.exceptions.DrinkPriceNotFoundException;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkCategory;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkPriceRepository;
import com.rokzasok.serveit.repository.DrinkRepository;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rokzasok.serveit.constants.PriceConstants.ID2;
import static com.rokzasok.serveit.constants.PriceConstants.IS_DELETED2;
import static com.rokzasok.serveit.constants.PriceConstants.*;
import static com.rokzasok.serveit.constants.UserSalaryConstants.ID1;
import static com.rokzasok.serveit.constants.UserSalaryConstants.IS_DELETED1;
import static com.rokzasok.serveit.constants.UserSalaryConstants.NON_EXISTING_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class DrinkPriceServiceUnitTest {

    @Autowired
    DrinkPriceService drinkPriceService;

    @MockBean
    DrinkPriceRepository drinkPriceRepository;

    @MockBean
    DrinkRepository drinkRepository;

    @Test
    public void testFindOne_IdExisting_ShouldReturn_DrinkPrice() {
        Drink drink = Drink.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DrinkCategory.NON_ALCOHOLIC)
                .code("drink1")
                .name("First drink")
                .description("The very first drink ever made.")
                .ingredients("")
                .purchasePrice(500.0)
                .isDeleted(false)
                .build();
        DrinkPrice price1 = DrinkPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .drink(drink)
                .build();

        when(drinkPriceRepository.findById(ID1)).thenReturn(Optional.ofNullable(price1));

        DrinkPrice found = drinkPriceService.findOne(ID1);

        assertNotNull(found);
        assertEquals(price1.getId(), found.getId());
        assertEquals(price1.getDrink().getId(), found.getDrink().getId());
        assertEquals(price1.getPriceDate(), found.getPriceDate());
        assertEquals(price1.getPrice(), found.getPrice());
        assertFalse(found.getIsDeleted());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        when(drinkPriceRepository.findById(ID1)).thenReturn(Optional.empty());

        DrinkPrice found = drinkPriceService.findOne(ID1);

        assertNull(found);
    }

    // TODO maybe unnecessary everywhere
    @Test
    public void testFindAll_ShouldReturn_List() {
        Drink drink = Drink.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DrinkCategory.NON_ALCOHOLIC)
                .code("drink1")
                .name("First drink")
                .description("The very first drink ever made.")
                .ingredients("")
                .purchasePrice(500.0)
                .isDeleted(false)
                .build();
        DrinkPrice price1 = DrinkPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .drink(drink)
                .build();
        DrinkPrice price2 = DrinkPrice.builder()
                .id(ID2)
                .price(PRICE2)
                .priceDate(PRICE_DATE2)
                .isDeleted(IS_DELETED2)
                .drink(drink)
                .build();

        List<DrinkPrice> prices = new ArrayList<>();
        prices.add(price1);
        prices.add(price2);

        when(drinkPriceRepository.findAll()).thenReturn(prices);

        List<DrinkPrice> found = drinkPriceService.findAll();
        assertEquals(2, found.size());
    }

    // TODO unnecessary, ima smisla samo kao integracioni test
    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        Drink drink = Drink.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DrinkCategory.NON_ALCOHOLIC)
                .code("drink1")
                .name("First drink")
                .description("The very first drink ever made.")
                .ingredients("")
                .purchasePrice(500.0)
                .isDeleted(false)
                .build();
        DrinkPrice price1 = DrinkPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .drink(drink)
                .build();

        when(drinkPriceRepository.findById(ID1)).thenReturn(Optional.ofNullable(price1));

        Boolean deleted = drinkPriceService.deleteOne(ID1);

        assertEquals(true, deleted);
    }

    @Test(expected = DrinkPriceNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        when(drinkPriceRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        drinkPriceService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChanged() throws Exception {
        Drink drink = Drink.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DrinkCategory.NON_ALCOHOLIC)
                .code("drink1")
                .name("First drink")
                .description("The very first drink ever made.")
                .ingredients("")
                .purchasePrice(500.0)
                .isDeleted(false)
                .build();
        DrinkPrice price1 = DrinkPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .drink(drink)
                .build();

        when(drinkPriceRepository.findById(ID1)).thenReturn(Optional.ofNullable(price1));

        DrinkPriceDTO changedDTO = DrinkPriceDTO.builder()
                .id(ID1)
                .priceDate(LocalDate.now())
                .price(450.0)
                .drinkId(1)
                .build();

        DrinkPrice changed = DrinkPrice.builder()
                .id(ID2)
                .isDeleted(IS_DELETED1)
                .price(450.0)
                .priceDate(LocalDate.now())
                .drink(drink)
                .build();

        when(drinkPriceRepository.save(any(DrinkPrice.class))).thenReturn(changed);
        when(drinkRepository.findById(1)).thenReturn(Optional.of(drink));

        DrinkPrice edited = drinkPriceService.edit(1, changedDTO);
        assertNotEquals(price1.getId(), edited.getId());
        assertEquals(changed.getId(), edited.getId());
    }

    @Test(expected = DrinkPriceNotFoundException.class)
    public void testEdit_IdNotExisting_ShouldThrow_DrinkPriceNotFound() throws Exception {
        Drink drink = Drink.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DrinkCategory.NON_ALCOHOLIC)
                .code("drink1")
                .name("First drink")
                .description("The very first drink ever made.")
                .ingredients("")
                .purchasePrice(500.0)
                .isDeleted(false)
                .build();
        when(drinkPriceRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(drinkRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.ofNullable(drink));

        DrinkPriceDTO empty = DrinkPriceDTO.builder()
                .id(NON_EXISTING_ID)
                .drinkId(NON_EXISTING_ID)
                .price(3000.0)
                .priceDate(LocalDate.now())
                .build();

        drinkPriceService.edit(NON_EXISTING_ID, empty);
    }

    @Test(expected = DrinkNotFoundException.class)
    public void testEdit_UserIdNotExisting_ShouldThrow_DrinkNotFound() throws Exception {
        Drink drink = Drink.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DrinkCategory.NON_ALCOHOLIC)
                .code("drink1")
                .name("First drink")
                .description("The very first drink ever made.")
                .ingredients("")
                .purchasePrice(500.0)
                .isDeleted(false)
                .build();
        DrinkPrice price1 = DrinkPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .drink(drink)
                .build();
        when(drinkPriceRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.ofNullable(price1));
        when(drinkRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        DrinkPriceDTO empty = DrinkPriceDTO.builder()
                .id(NON_EXISTING_ID)
                .drinkId(NON_EXISTING_ID)
                .price(3000.0)
                .priceDate(LocalDate.now())
                .build();

        drinkPriceService.edit(NON_EXISTING_ID, empty);
    }

}
