package com.rokzasok.serveit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "SittingTable Not Found")
public class SittingTableNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public SittingTableNotFoundException(String message) {
        super(message);
    }
}
