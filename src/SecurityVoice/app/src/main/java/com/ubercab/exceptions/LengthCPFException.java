package com.ubercab.exceptions;

public class LengthCPFException extends RuntimeException {
    public LengthCPFException(String message) {
        super(message);
        System.out.println(message);
    }
}
