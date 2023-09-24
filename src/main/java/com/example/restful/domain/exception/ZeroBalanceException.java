package com.example.restful.domain.exception;

public class ZeroBalanceException extends RuntimeException {

    public ZeroBalanceException(final String message) {
        super(message);
    }
}
