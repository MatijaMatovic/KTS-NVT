package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.converters.DrinkMenuToDrinkMenuDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.repository.DrinkMenuRepository;
import com.rokzasok.serveit.service.impl.DrinkMenuService;
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

import static com.rokzasok.serveit.constants.MenuConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.properties")
public class DrinkMenuServiceUnitTest {

    @Autowired
    DrinkMenuService drinkMenuService;

    @MockBean
    DrinkMenuRepository drinkMenuRepository;

    @Autowired
    DrinkMenuToDrinkMenuDTO converter;

    DrinkMenu menu1;
    DrinkMenu menu2;
    DrinkMenu menu3;
    DrinkMenu empty;

    @PostConstruct
    public void setup() {
        menu1 = DrinkMenu.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .build();

        menu2 = DrinkMenu.builder()
                .id(ID2)
                .isDeleted(IS_DELETED2)
                .build();

        menu3 = DrinkMenu.builder()
                .id(ID3)
                .isDeleted(IS_DELETED3)
                .build();

        empty = DrinkMenu.builder()
                .id(NON_EXISTING_ID)
                .build();

        List<DrinkMenu> menus = new ArrayList<>();
        menus.add(menu1);
        menus.add(menu2);

        // testFindAll
        given(drinkMenuRepository.findAll()).willReturn(menus);

        // testFindOne
        given(drinkMenuRepository.findById(ID1)).willReturn(Optional.ofNullable(menu1));
        // testFindOne
        given(drinkMenuRepository.findById(ID2)).willReturn(Optional.ofNullable(menu2));

        // testFindOne_NonExistingID
        Optional<DrinkMenu> drinkMenuNull = Optional.empty(); //Da bi orElse mogao da vrati Null
        given(drinkMenuRepository.findById(NON_EXISTING_ID)).willReturn(drinkMenuNull);
        doNothing().when(drinkMenuRepository).delete(menu1);

        // testEdit
        given(drinkMenuRepository.findById(ID2)).willReturn(Optional.ofNullable(menu2));
        given(drinkMenuRepository.save(any())).willReturn(menu3);

        // testLast
        List<DrinkMenu> menus2 = new ArrayList<>(menus);
        menus2.add(menu3);
        given(drinkMenuRepository.findTopByOrderByDateDesc()).willReturn(menu3);

    }

    @Test
    public void testFindAll() {
        List<DrinkMenu> found = drinkMenuService.findAll();

        verify(drinkMenuRepository, times(1)).findAll();
        assertEquals(2, found.size());
    }

    @Test
    public void testDelete() {
        Boolean deleted = drinkMenuService.deleteOne(ID1);

        verify(drinkMenuRepository, times(1)).findById(ID1);
        assertEquals(true, deleted);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDelete_NonExistingID() {
        Boolean deleted = drinkMenuService.deleteOne(NON_EXISTING_ID);

        assertEquals(false, deleted);
    }

    @Test
    public void testEdit() {
        DrinkMenu editedDrinkMenu = drinkMenuService.edit(menu2.getId(), converter.convert(menu2));

        verify(drinkMenuRepository, times(1)).findById(ID2);
        assertNotEquals(editedDrinkMenu.getId(), menu2.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEdit_NonExistingID() {
        DrinkMenu editedDrinkMenu = drinkMenuService.edit(empty.getId(), converter.convert(empty));

        verify(drinkMenuRepository, times(1)).findById(ID2);
        assertEquals(editedDrinkMenu.getId(), menu2.getId());
    }

    @Test
    public void testLast() {
        DrinkMenu menu = drinkMenuService.last();
        assertEquals(ID3, menu.getId());
    }

}
