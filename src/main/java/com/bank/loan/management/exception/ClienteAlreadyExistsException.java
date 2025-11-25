package com.bank.loan.management.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ClienteAlreadyExistsException extends RuntimeException {
    public ClienteAlreadyExistsException(String message) {
        super(message);
    }
}
