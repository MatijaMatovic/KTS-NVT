package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Drink Not Found")
public class DrinkNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public DrinkNotFoundException(String message) {
        super(message);
    }
}
