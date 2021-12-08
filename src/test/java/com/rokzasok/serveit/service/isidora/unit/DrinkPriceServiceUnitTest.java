package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.repository.DrinkPriceRepository;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
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
public class DrinkPriceServiceUnitTest {

    @Autowired
    DrinkPriceService drinkPriceService;

    @MockBean
    DrinkPriceRepository drinkPriceRepository;

    DrinkPrice price1;
    DrinkPrice price2;
    DrinkPrice price3;
    DrinkPrice empty;

    @PostConstruct
    public void setup() {
        price1 = DrinkPrice.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .price(PRICE1)
                .priceDate(PRICE_DATE1)
                .drink(null)
                .build();

        price2 = DrinkPrice.builder()
                .id(ID2)
                .price(PRICE2)
                .priceDate(PRICE_DATE2)
                .isDeleted(IS_DELETED2)
                .drink(null)
                .build();

        price3 = DrinkPrice.builder()
                .id(ID3)
                .price(PRICE3)
                .priceDate(PRICE_DATE3)
                .isDeleted(IS_DELETED3)
                .drink(null)
                .build();

        empty = DrinkPrice.builder()
                .id(NON_EXISTING_ID)
                .build();

        List<DrinkPrice> prices = new ArrayList<>();
        prices.add(price1);
        prices.add(price2);

        // testFindAll
        given(drinkPriceRepository.findAll()).willReturn(prices);

        // testFindOne
        given(drinkPriceRepository.findById(ID1)).willReturn(Optional.ofNullable(price1));
        // testFindOne
        given(drinkPriceRepository.findById(ID2)).willReturn(Optional.ofNullable(price2));

        // testFindOne_NonExistingID
        Optional<DrinkPrice> drinkPriceNull = Optional.empty(); //Da bi orElse mogao da vrati Null
        given(drinkPriceRepository.findById(NON_EXISTING_ID)).willReturn(drinkPriceNull);
        doNothing().when(drinkPriceRepository).delete(price1);

    }

    @Test
    public void testFindAll() {
        List<DrinkPrice> found = drinkPriceService.findAll();

        verify(drinkPriceRepository, times(1)).findAll();
        assertEquals(2, found.size());
    }

    @Test
    public void testDelete() {
        Boolean deleted = drinkPriceService.deleteOne(ID1);

        verify(drinkPriceRepository, times(1)).findById(ID1);
        assertEquals(true, deleted);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDelete_NonExistingID() {
        Boolean deleted = drinkPriceService.deleteOne(NON_EXISTING_ID);

        assertEquals(false, deleted);
    }

}
