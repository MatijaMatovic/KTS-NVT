package com.rokzasok.serveit.constants;

import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkCategory;

public class DrinkConstants {

    public static final Integer CORRECT_ID = 1;
    public static final Integer WRONG_ID = -1;

    public static final int NUMBER_OF_INSTANCES = 3;

    public static final Drink NEW_ID_DRINK = new Drink(4, "12356", "Fanta", DrinkCategory.NON_ALCOHOLIC, "Nema", "Narandza, emulgatori", 35.0, "Bolja je Coca Cola", "via.placeholder.com/640x360", false);
    public static final Drink EXISTING_ID_DRINK = new Drink(1, "12356", "Fanta", DrinkCategory.NON_ALCOHOLIC, "Nema", "Narandza, emulgatori", 35.0, "Bolja je Coca Cola", "via.placeholder.com/640x360", false);
    public static final Drink NO_ID_DRINK = new Drink(null, "12356", "Fanta", DrinkCategory.NON_ALCOHOLIC, "Nema", "Narandza, emulgatori", 35.0, "Bolja je Coca Cola", "via.placeholder.com/640x360", false);

}
