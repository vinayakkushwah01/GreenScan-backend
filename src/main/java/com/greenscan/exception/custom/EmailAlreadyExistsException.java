package com.greenscan.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to throw when a user tries to register with an existing email.
 */
@ResponseStatus(HttpStatus.CONFLICT) // HTTP 409 Conflict
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("User with email " + email + " already exists.");
    }
}
