package com.example.restful.domain.exception;

public class WrongPinException extends RuntimeException {

    public WrongPinException(final String message) {
        super(message);
    }
}
