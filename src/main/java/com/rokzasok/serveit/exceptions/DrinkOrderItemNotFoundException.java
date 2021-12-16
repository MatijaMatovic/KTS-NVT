package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "DrinkOrderItem Not Found")
public class DrinkOrderItemNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;

    public DrinkOrderItemNotFoundException(String message) {
        super(message);
    }
}
