package com.rokzasok.serveit.constants;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;

public class DishOrderItemConstants {
    public static final Integer CORRECT_ID = 1;
    public static final Integer WRONG_ID = -1;

    public static final DishOrderItem NEW_ID_DISH_ORDER_ITEM = new DishOrderItem(7, ItemStatus.CREATED, "Pozuriti", 1, 1, false, null, null);
    public static final DishOrderItem EXISTING_ID_DISH_ORDER_ITEM = new DishOrderItem(1, ItemStatus.CREATED, "Pozuriti", 1, 1, false, null, null);
    public static final DishOrderItem NO_ID_DISH_ORDER_ITEM = new DishOrderItem(null, ItemStatus.CREATED, "Pozuriti", 1, 1, false, null, null);


    public static final int NUMBER_OF_INSTANCES = 6;

    public static final Integer TEST_COOK_ID = 3;
    public static final Integer WRONG_TEST_COOK_ID = -1;
    public static final int NUMBER_OF_ITEM_ORDERS_FOR_TEST_COOK_ID = 3;

    public static final Integer ID_WITH_READY_STATUS = 5;

}
