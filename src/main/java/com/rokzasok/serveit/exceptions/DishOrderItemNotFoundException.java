package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "DishOrderItem Not Found")
public class DishOrderItemNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;

    public DishOrderItemNotFoundException(String message) {
        super(message);
    }
}
