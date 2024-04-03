package org.gabrielbarrilli.securitytelegram.exception;

public class CpfUniqueViolationException extends RuntimeException {
    public CpfUniqueViolationException(String s) {
        super(s);
    }
}
