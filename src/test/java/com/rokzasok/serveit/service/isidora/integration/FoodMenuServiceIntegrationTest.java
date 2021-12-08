package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.converters.FoodMenuToFoodMenuDTO;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.repository.FoodMenuRepository;
import com.rokzasok.serveit.service.impl.DishService;
import com.rokzasok.serveit.service.impl.FoodMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.rokzasok.serveit.constants.MenuConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class FoodMenuServiceIntegrationTest {

    @Autowired
    private FoodMenuService foodMenuService;
    @Autowired
    private FoodMenuRepository foodMenuRepository;
    @Autowired
    private DishService dishService;
    @Autowired
    private FoodMenuToFoodMenuDTO converter;

    @Test
    public void testFindAll() {
        List<FoodMenu> found = foodMenuService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS, found.size());
    }

    @Test
    public void testFindById() {
        FoodMenu found = foodMenuService.findOne(ID1);
        assertEquals(ID1, found.getId());
    }

    @Test
    public void testSave(){
        FoodMenu foodMenu = FoodMenu.builder().id(null).date(DATE1).dishes(new HashSet<>()).isDeleted(false).build();
        int oldAll = foodMenuService.findAll().size();
        FoodMenu created = foodMenuService.save(foodMenu);
        int newAll = foodMenuService.findAll().size();

        assertNotEquals(oldAll, newAll);
    }

    @Test
    public void testDelete(){
        foodMenuService.deleteOne(ID_TO_DELETE);
        FoodMenu foodMenu = foodMenuService.findOne(ID_TO_DELETE);
        assertNull(foodMenu);
    }

    @Test
    public void testLast() {
        List<FoodMenu> all = foodMenuRepository.findAll();
        FoodMenu last = foodMenuService.last();
        assertTrue(all.get(0).getDate().isBefore(last.getDate()));
    }

    @Test
    public void testEdit_WithoutDishes() {
        FoodMenu toEdit = foodMenuService.findOne(ID_TO_EDIT1);
        LocalDate beforeEditDate = toEdit.getDate();
        toEdit.setDate(beforeEditDate.plusDays(13));
        FoodMenu edited = foodMenuService.edit(ID_TO_EDIT1, converter.convert(toEdit));
        assertEquals(ID_TO_EDIT1, edited.getId());
        assertNotEquals(beforeEditDate, edited.getDate());
        assertNotNull(foodMenuService.findOne(ID_TO_EDIT1));
    }

    @Test
    public void testEdit_WithDishes() {
        FoodMenu toEdit = foodMenuService.findOne(ID_TO_EDIT1);
        int beforeEditDishesSize = toEdit.getDishes().size();
        Dish d = dishService.findOne(1);
        DishPrice newPrice = DishPrice.builder().id(300).dish(d).priceDate(LocalDate.now()).price(450.0).build();
        toEdit.getDishes().add(newPrice);
        FoodMenu edited = foodMenuService.edit(ID_TO_EDIT1, converter.convert(toEdit));
        assertEquals(ID_TO_EDIT1, edited.getId());
        assertNotEquals(beforeEditDishesSize, edited.getDishes().size());
        assertNotNull(foodMenuService.findOne(ID_TO_EDIT1));
    }

}
