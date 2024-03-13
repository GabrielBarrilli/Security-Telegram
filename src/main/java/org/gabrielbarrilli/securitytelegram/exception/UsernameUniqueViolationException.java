package org.gabrielbarrilli.securitytelegram.exception;

public class UsernameUniqueViolationException extends RuntimeException {
    public UsernameUniqueViolationException(String s) {
        super(s);
    }
}
