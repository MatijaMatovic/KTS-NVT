package com.rokzasok.serveit.repository.isidora;

import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.repository.DrinkMenuRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static com.rokzasok.serveit.constants.MenuConstants.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class DrinkMenuRepositoryIntegrationTest {

    @Autowired
    private DrinkMenuRepository drinkMenuRepository;

    @Before
    public void setUp() {
        DrinkMenu drinkMenu1 = DrinkMenu.builder().id(ID1).date(DATE1).isDeleted(IS_DELETED1).drinks(new HashSet<>()).build();
        drinkMenuRepository.save(drinkMenu1);

        DrinkMenu drinkMenu2 = DrinkMenu.builder().id(ID2).date(DATE2).isDeleted(IS_DELETED2).drinks(new HashSet<>()).build();
        drinkMenuRepository.save(drinkMenu2);
    }

    @Test
    public void testFindTopByOrderByDateDesc() {
        DrinkMenu found = drinkMenuRepository.findTopByOrderByDateDesc();
        assertTrue("Last menu should be after all other menus", found.getDate().isAfter(DATE1));
    }

}
