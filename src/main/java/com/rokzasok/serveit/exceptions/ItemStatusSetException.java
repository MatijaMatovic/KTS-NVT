package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Bad Request")
public class ItemStatusSetException extends Exception{
    private static final long serialVersionUID = 1L;

    public ItemStatusSetException(String message) {
        super(message);
    }
}