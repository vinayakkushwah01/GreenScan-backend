package com.greenscan.exception.custom;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String msg){

        super(msg);
    }

}
