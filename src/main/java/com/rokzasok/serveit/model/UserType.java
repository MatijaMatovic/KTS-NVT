package com.rokzasok.serveit.model;

public enum UserType {
    ADMINISTRATOR("ADMINISTRATOR"), DIRECTOR("DIRECTOR"), MANAGER("MANAGER"), WAITER("WAITER"), COOK("COOK"), BARTENDER("BARTENDER"), CHEF("CHEF");

    UserType(String role) {
    }
}
