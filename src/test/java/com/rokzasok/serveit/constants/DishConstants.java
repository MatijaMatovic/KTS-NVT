package com.rokzasok.serveit.constants;

import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.model.DishCategory;

public class DishConstants {
    public static final Integer CORRECT_ID = 1;
    public static final Integer WRONG_ID = -1;

    public static final int NUMBER_OF_INSTANCES = 6;

    public static final Dish NEW_ID_DISH = new Dish(7, DishCategory.BREAKFAST, "234567", "Hleb sa Nutellom", "Nema", "Hleb, Nutella", "Namazati Nutellu na hleb", 50.0, "Ukusno i jednostavno", "via.placeholder.com/640x360", 5, false);
    public static final Dish EXISTING_ID_DISH = new Dish(1, DishCategory.BREAKFAST, "234567", "Hleb sa Nutellom", "Nema", "Hleb, Nutella", "Namazati Nutellu na hleb", 50.0, "Ukusno i jednostavno", "via.placeholder.com/640x360", 5, false);
    public static final Dish NO_ID_DISH = new Dish(null, DishCategory.BREAKFAST, "234567", "Hleb sa Nutellom", "Nema", "Hleb, Nutella", "Namazati Nutellu na hleb", 50.0, "Ukusno i jednostavno", "via.placeholder.com/640x360", 5, false);

}
