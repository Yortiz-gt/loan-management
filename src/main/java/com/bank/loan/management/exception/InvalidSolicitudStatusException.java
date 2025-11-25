package com.bank.loan.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSolicitudStatusException extends RuntimeException {
    public InvalidSolicitudStatusException(String message) {
        super(message);
    }
}
