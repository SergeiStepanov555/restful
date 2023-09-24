package com.example.restful.domain.exception;

public class WrongAmountException extends RuntimeException {

    public WrongAmountException(final String message) {
        super(message);
    }
}
