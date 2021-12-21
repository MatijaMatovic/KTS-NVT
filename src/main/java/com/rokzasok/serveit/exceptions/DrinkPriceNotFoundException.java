package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "DrinkPrice Not Found")
public class DrinkPriceNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public DrinkPriceNotFoundException(String message) {
        super(message);
    }
}
