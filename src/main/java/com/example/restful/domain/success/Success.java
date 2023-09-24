package com.example.restful.domain.success;

public enum Success {

    SUCCESS_DEPOSIT("Deposit success"),
    SUCCESS_TRANSFER("Transfer success"),
    SUCCESS_WITHDRAW("Withdraw success");

    private final String valueOfSuccess;

    Success(String valueOfSuccess) {
        this.valueOfSuccess = valueOfSuccess;
    }
}
