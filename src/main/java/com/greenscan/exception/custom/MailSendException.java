package com.greenscan.exception.custom;

public class MailSendException extends RuntimeException {

    // Constructor with a custom message
    public MailSendException(String message) {
        super(message);
    }
}