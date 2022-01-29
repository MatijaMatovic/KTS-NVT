package com.rokzasok.serveit.service.isidora.integration;

import com.rokzasok.serveit.converters.DrinkMenuToDrinkMenuDTO;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.repository.DrinkMenuRepository;
import com.rokzasok.serveit.service.impl.DrinkService;
import com.rokzasok.serveit.service.impl.DrinkMenuService;
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
import static com.rokzasok.serveit.constants.MenuConstants.ID_TO_EDIT1;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DrinkMenuServiceIntegrationTest {

    @Autowired
    private DrinkMenuService drinkMenuService;
    @Autowired
    private DrinkMenuRepository drinkMenuRepository;
    @Autowired
    private DrinkService drinkService;
    @Autowired
    private DrinkMenuToDrinkMenuDTO converter;

    @Test
    public void testFindAll() {
        List<DrinkMenu> found = drinkMenuService.findAll();
        assertEquals(FIND_ALL_NUMBER_OF_ITEMS, found.size());
    }

    @Test
    public void testFindById() {
        DrinkMenu found = drinkMenuService.findOne(ID1);
        assertEquals(ID1, found.getId());
    }

    @Test
    public void testSave(){
        DrinkMenu drinkMenu = DrinkMenu.builder().id(null).date(DATE1).drinks(new HashSet<>()).isDeleted(false).build();
        int oldAll = drinkMenuService.findAll().size();
        DrinkMenu created = drinkMenuService.save(drinkMenu);
        int newAll = drinkMenuService.findAll().size();

        assertNotEquals(oldAll, newAll);
    }

    @Test
    public void testDelete(){
        drinkMenuService.deleteOne(ID_TO_DELETE);
        DrinkMenu drinkMenu = drinkMenuService.findOne(ID_TO_DELETE);
        assertNull(drinkMenu);
    }

    @Test
    public void testLast() {
        List<DrinkMenu> all = drinkMenuRepository.findAll();
        DrinkMenu last = drinkMenuService.last();
        assertTrue(all.get(0).getDate().isBefore(last.getDate()));
    }

    @Test
    public void testEdit_WithoutDrinkes() throws Exception {
        DrinkMenu toEdit = drinkMenuService.findOne(ID_TO_EDIT1);
        LocalDate beforeEditDate = toEdit.getDate();
        toEdit.setDate(beforeEditDate.plusDays(13));
        DrinkMenu edited = drinkMenuService.edit(ID_TO_EDIT1, converter.convert(toEdit));
        assertEquals(ID_TO_EDIT1, edited.getId());
        assertNotEquals(beforeEditDate, edited.getDate());
        assertNotNull(drinkMenuService.findOne(ID_TO_EDIT1));
    }

    @Test
    public void testEdit_WithDrinkes() throws Exception {
        DrinkMenu toEdit = drinkMenuService.findOne(ID_TO_EDIT1);
        int beforeEditDrinksSize = toEdit.getDrinks().size();
        Drink d = drinkService.findOne(1);
        DrinkPrice newPrice = DrinkPrice.builder().id(300).drink(d).priceDate(LocalDate.now()).price(450.0).build();
        toEdit.getDrinks().add(newPrice);
        DrinkMenu edited = drinkMenuService.edit(ID_TO_EDIT1, converter.convert(toEdit));
        assertEquals(ID_TO_EDIT1, edited.getId());
        assertNotEquals(beforeEditDrinksSize, edited.getDrinks().size());
        assertNotNull(drinkMenuService.findOne(ID_TO_EDIT1));
    }

}
