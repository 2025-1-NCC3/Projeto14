package br.fecap.pi.securityvoice.exceptions;

public class LengthCPFException extends RuntimeException {
    public LengthCPFException(String message) {
        super(message);
        System.out.println(message);
    }
}
