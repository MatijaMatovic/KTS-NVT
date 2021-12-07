package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.FoodMenu;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static com.rokzasok.serveit.constatns.MenuConstants.*;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class FoodMenuRepositoryUnitTest {

    @Autowired
    private FoodMenuRepository foodMenuRepository;

    @Before
    public void setUp() {
        FoodMenu foodMenu1 = FoodMenu.builder().id(ID1).date(DATE1).isDeleted(IS_DELETED1).dishes(new HashSet<>()).build();
        foodMenuRepository.save(foodMenu1);

        FoodMenu foodMenu2 = FoodMenu.builder().id(ID2).date(DATE2).isDeleted(IS_DELETED2).dishes(new HashSet<>()).build();
        foodMenuRepository.save(foodMenu2);
    }

    @Test
    public void testFindTopByOrderByDateDesc() {
        FoodMenu found = foodMenuRepository.findTopByOrderByDateDesc();
        assertTrue(found.getDate().isAfter(DATE1));
    }

}
