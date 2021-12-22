package com.rokzasok.serveit.service.isidora.unit;

import com.rokzasok.serveit.converters.FoodMenuToFoodMenuDTO;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.repository.FoodMenuRepository;
import com.rokzasok.serveit.service.impl.FoodMenuService;
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
public class FoodMenuServiceUnitTest {

    @Autowired
    FoodMenuService foodMenuService;

    @MockBean
    FoodMenuRepository foodMenuRepository;

    @Autowired
    FoodMenuToFoodMenuDTO converter;

    FoodMenu menu1;
    FoodMenu menu2;
    FoodMenu menu3;
    FoodMenu empty;

    @PostConstruct
    public void setup() {
        menu1 = FoodMenu.builder()
                .id(ID1)
                .isDeleted(IS_DELETED1)
                .build();

        menu2 = FoodMenu.builder()
                .id(ID2)
                .isDeleted(IS_DELETED2)
                .build();

        menu3 = FoodMenu.builder()
                .id(ID3)
                .isDeleted(IS_DELETED3)
                .build();

        empty = FoodMenu.builder()
                .id(NON_EXISTING_ID)
                .build();

        List<FoodMenu> menus = new ArrayList<>();
        menus.add(menu1);
        menus.add(menu2);

        // testFindAll
        given(foodMenuRepository.findAll()).willReturn(menus);

        // testFindOne
        given(foodMenuRepository.findById(ID1)).willReturn(Optional.ofNullable(menu1));
        // testFindOne
        given(foodMenuRepository.findById(ID2)).willReturn(Optional.ofNullable(menu2));

        // testFindOne_NonExistingID
        Optional<FoodMenu> foodMenuNull = Optional.empty(); //Da bi orElse mogao da vrati Null
        given(foodMenuRepository.findById(NON_EXISTING_ID)).willReturn(foodMenuNull);
        doNothing().when(foodMenuRepository).delete(menu1);

        // testEdit
        given(foodMenuRepository.findById(ID2)).willReturn(Optional.ofNullable(menu2));
        given(foodMenuRepository.save(any())).willReturn(menu3);

        // testLast
        List<FoodMenu> menus2 = new ArrayList<>(menus);
        menus2.add(menu3);
        given(foodMenuRepository.findTopByOrderByDateDesc()).willReturn(menu3);

    }

    @Test
    public void testFindAll() {
        List<FoodMenu> found = foodMenuService.findAll();

        verify(foodMenuRepository, times(1)).findAll();
        assertEquals(2, found.size());
    }

    @Test
    public void testDelete() {
        Boolean deleted = foodMenuService.deleteOne(ID1);

        verify(foodMenuRepository, times(1)).findById(ID1);
        assertEquals(true, deleted);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDelete_NonExistingID() {
        Boolean deleted = foodMenuService.deleteOne(NON_EXISTING_ID);

        assertEquals(false, deleted);
    }

    @Test
    public void testEdit() throws Exception {
        FoodMenu editedFoodMenu = foodMenuService.edit(menu2.getId(), converter.convert(menu2));

        verify(foodMenuRepository, times(1)).findById(ID2);
        assertNotEquals(editedFoodMenu.getId(), menu2.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEdit_NonExistingID() throws Exception {
        FoodMenu editedFoodMenu = foodMenuService.edit(empty.getId(), converter.convert(empty));

        verify(foodMenuRepository, times(1)).findById(ID2);
        assertEquals(editedFoodMenu.getId(), menu2.getId());
    }

    @Test
    public void testLast() {
        FoodMenu menu = foodMenuService.last();
        assertEquals(ID3, menu.getId());
    }


}
