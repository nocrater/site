package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class PhoneDoesNotExistException extends RuntimeException {
    public PhoneDoesNotExistException(long phoneId) {
        super("Phone " + phoneId + " does not exist.");
    }
}
