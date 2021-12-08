package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.repository.DishPriceRepository;
import com.rokzasok.serveit.service.impl.DishPriceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rokzasok.serveit.constants.PriceConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class DishPriceServiceUnitTest {

    @Autowired
    DishPriceService dishPriceService;

    @MockBean
    DishPriceRepository dishPriceRepository;

    DishPrice price1;
    DishPrice price2;
    DishPrice price3;
    DishPrice empty;

    @PostConstruct
    public void setup() {
        price1 = DishPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .dish(null)
                .build();

        price2 = DishPrice.builder()
                .id(ID2)
                .price(PRICE2)
                .priceDate(PRICE_DATE2)
                .isDeleted(IS_DELETED2)
                .dish(null)
                .build();

        price3 = DishPrice.builder()
                .id(ID3)
                .price(PRICE3)
                .priceDate(PRICE_DATE3)
                .isDeleted(IS_DELETED3)
                .dish(null)
                .build();

        empty = DishPrice.builder()
                .id(NON_EXISTING_ID)
                .build();

        List<DishPrice> prices = new ArrayList<>();
        prices.add(price1);
        prices.add(price2);

        // testFindAll
        given(dishPriceRepository.findAll()).willReturn(prices);

        // testFindOne
        given(dishPriceRepository.findById(ID1)).willReturn(Optional.ofNullable(price1));
        // testFindOne
        given(dishPriceRepository.findById(ID2)).willReturn(Optional.ofNullable(price2));

        // testFindOne_NonExistingID
        Optional<DishPrice> dishPriceNull = Optional.empty(); //Da bi orElse mogao da vrati Null
        given(dishPriceRepository.findById(NON_EXISTING_ID)).willReturn(dishPriceNull);
        doNothing().when(dishPriceRepository).delete(price1);

    }

    @Test
    public void testFindAll() {
        List<DishPrice> found = dishPriceService.findAll();

        verify(dishPriceRepository, times(1)).findAll();
        assertEquals(2, found.size());
    }

    @Test
    public void testDelete() {
        Boolean deleted = dishPriceService.deleteOne(ID1);

        verify(dishPriceRepository, times(1)).findById(ID1);
        assertEquals(true, deleted);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDelete_NonExistingID() {
        Boolean deleted = dishPriceService.deleteOne(NON_EXISTING_ID);

        assertEquals(false, deleted);
    }

}
