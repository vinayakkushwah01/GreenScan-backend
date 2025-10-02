package com.greenscan.exception.custom;

public class MobileAlreadyExistsException extends RuntimeException {
    public MobileAlreadyExistsException(String mobile) {
        super("Mobile number already exists: " + mobile);
    }
}
