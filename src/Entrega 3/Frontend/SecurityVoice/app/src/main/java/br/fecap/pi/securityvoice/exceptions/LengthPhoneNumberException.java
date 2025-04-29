package br.fecap.pi.securityvoice.exceptions;

public class LengthPhoneNumberException extends RuntimeException {
    public LengthPhoneNumberException(String message) {
        super(message);
        System.out.println(message);
    }
}
