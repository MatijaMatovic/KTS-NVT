package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.exceptions.DishNotFoundException;
import com.rokzasok.serveit.exceptions.DishPriceNotFoundException;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.model.DishCategory;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.repository.DishPriceRepository;
import com.rokzasok.serveit.repository.DishRepository;
import com.rokzasok.serveit.service.impl.DishPriceService;
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
public class DishPriceServiceUnitTest {

    @Autowired
    DishPriceService dishPriceService;

    @MockBean
    DishPriceRepository dishPriceRepository;

    @MockBean
    DishRepository dishRepository;

    @Test
    public void testFindOne_IdExisting_ShouldReturn_DishPrice() {
        Dish dish = Dish.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DishCategory.BREAKFAST)
                .code("dish1")
                .name("First dish")
                .description("The very first dish ever made.")
                .ingredients("")
                .preparationTime(30)
                .preparationPrice(500.0)
                .recipe("Just mix everything you have and cook for 30 minutes.")
                .isDeleted(false)
                .build();
        DishPrice price1 = DishPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .dish(dish)
                .build();

        when(dishPriceRepository.findById(ID1)).thenReturn(Optional.ofNullable(price1));

        DishPrice found = dishPriceService.findOne(ID1);

        assertNotNull(found);
        assertEquals(price1.getId(), found.getId());
        assertEquals(price1.getDish().getId(), found.getDish().getId());
        assertEquals(price1.getPriceDate(), found.getPriceDate());
        assertEquals(price1.getPrice(), found.getPrice());
        assertFalse(found.getIsDeleted());
    }

    @Test
    public void testFindOne_IdNotExisting_ShouldReturn_Null() {
        when(dishPriceRepository.findById(ID1)).thenReturn(Optional.empty());

        DishPrice found = dishPriceService.findOne(ID1);

        assertNull(found);
    }

    // TODO maybe unnecessary everywhere
    @Test
    public void testFindAll_ShouldReturn_List() {
        Dish dish = Dish.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DishCategory.BREAKFAST)
                .code("dish1")
                .name("First dish")
                .description("The very first dish ever made.")
                .ingredients("")
                .preparationTime(30)
                .preparationPrice(500.0)
                .recipe("Just mix everything you have and cook for 30 minutes.")
                .isDeleted(false)
                .build();
        DishPrice price1 = DishPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .dish(dish)
                .build();
        DishPrice price2 = DishPrice.builder()
                .id(ID2)
                .price(PRICE2)
                .priceDate(PRICE_DATE2)
                .isDeleted(IS_DELETED2)
                .dish(dish)
                .build();

        List<DishPrice> prices = new ArrayList<>();
        prices.add(price1);
        prices.add(price2);

        when(dishPriceRepository.findAll()).thenReturn(prices);

        List<DishPrice> found = dishPriceService.findAll();
        assertEquals(2, found.size());
    }

    // TODO unnecessary, ima smisla samo kao integracioni test
    @Test
    public void testDelete_IdExisting_ShouldReturn_True() throws Exception {
        Dish dish = Dish.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DishCategory.BREAKFAST)
                .code("dish1")
                .name("First dish")
                .description("The very first dish ever made.")
                .ingredients("")
                .preparationTime(30)
                .preparationPrice(500.0)
                .recipe("Just mix everything you have and cook for 30 minutes.")
                .isDeleted(false)
                .build();
        DishPrice price1 = DishPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .dish(dish)
                .build();

        when(dishPriceRepository.findById(ID1)).thenReturn(Optional.ofNullable(price1));

        Boolean deleted = dishPriceService.deleteOne(ID1);

        assertEquals(true, deleted);
    }

    @Test(expected = DishPriceNotFoundException.class)
    public void testDelete_IdNotExisting_ShouldThrow_TableNotFound() throws Exception {
        when(dishPriceRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        dishPriceService.deleteOne(NON_EXISTING_ID);
    }

    @Test
    public void testEdit_IdExisting_ShouldReturnChanged() throws Exception {
        Dish dish = Dish.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DishCategory.BREAKFAST)
                .code("dish1")
                .name("First dish")
                .description("The very first dish ever made.")
                .ingredients("")
                .preparationTime(30)
                .preparationPrice(500.0)
                .recipe("Just mix everything you have and cook for 30 minutes.")
                .isDeleted(false)
                .build();
        DishPrice price1 = DishPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .dish(dish)
                .build();

        when(dishPriceRepository.findById(ID1)).thenReturn(Optional.ofNullable(price1));

        DishPriceDTO changedDTO = DishPriceDTO.builder()
                .id(ID1)
                .priceDate(LocalDate.now())
                .price(450.0)
                .dishId(1)
                .build();

        DishPrice changed = DishPrice.builder()
                .id(ID2)
                .isDeleted(IS_DELETED1)
                .price(450.0)
                .priceDate(LocalDate.now())
                .dish(dish)
                .build();

        when(dishPriceRepository.save(any(DishPrice.class))).thenReturn(changed);
        when(dishRepository.findById(1)).thenReturn(Optional.of(dish));

        DishPrice edited = dishPriceService.edit(1, changedDTO);
        assertNotEquals(price1.getId(), edited.getId());
        assertEquals(changed.getId(), edited.getId());
    }

    @Test(expected = DishPriceNotFoundException.class)
    public void testEdit_IdNotExisting_ShouldThrow_DishPriceNotFound() throws Exception {
        Dish dish = Dish.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DishCategory.BREAKFAST)
                .code("dish1")
                .name("First dish")
                .description("The very first dish ever made.")
                .ingredients("")
                .preparationTime(30)
                .preparationPrice(500.0)
                .recipe("Just mix everything you have and cook for 30 minutes.")
                .isDeleted(false)
                .build();
        when(dishPriceRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(dishRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.ofNullable(dish));

        DishPriceDTO empty = DishPriceDTO.builder()
                .id(NON_EXISTING_ID)
                .dishId(NON_EXISTING_ID)
                .price(3000.0)
                .priceDate(LocalDate.now())
                .build();

        dishPriceService.edit(NON_EXISTING_ID, empty);
    }

    @Test(expected = DishNotFoundException.class)
    public void testEdit_UserIdNotExisting_ShouldThrow_DishNotFound() throws Exception {
        Dish dish = Dish.builder()
                .id(1)
                .allergens("")
                .imagePath("")
                .category(DishCategory.BREAKFAST)
                .code("dish1")
                .name("First dish")
                .description("The very first dish ever made.")
                .ingredients("")
                .preparationTime(30)
                .preparationPrice(500.0)
                .recipe("Just mix everything you have and cook for 30 minutes.")
                .isDeleted(false)
                .build();
        DishPrice price1 = DishPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .dish(dish)
                .build();
        when(dishPriceRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.ofNullable(price1));
        when(dishRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        DishPriceDTO empty = DishPriceDTO.builder()
                .id(NON_EXISTING_ID)
                .dishId(NON_EXISTING_ID)
                .price(3000.0)
                .priceDate(LocalDate.now())
                .build();

        dishPriceService.edit(NON_EXISTING_ID, empty);
    }

}
