package br.fecap.pi.securityvoice.exceptions;

public class LengthRGException extends RuntimeException {
    public LengthRGException(String message) {
        super(message);
        System.out.println(message);
    }
}
