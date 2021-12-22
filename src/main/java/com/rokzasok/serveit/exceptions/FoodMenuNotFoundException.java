package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "FoodMenu Not Found")
public class FoodMenuNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public FoodMenuNotFoundException(String message) {
        super(message);
    }
}
