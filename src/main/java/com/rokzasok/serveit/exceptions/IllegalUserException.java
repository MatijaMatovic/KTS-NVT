package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class IllegalUserException extends Exception{
    private static final long serialVersionUID = 1L;

    public IllegalUserException(String message) {
        super(message);
    }
}
