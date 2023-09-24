package com.example.restful.domain.account;

import lombok.Builder;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Builder
@Entity
@Table
public class Account {

    @Column
    @Id
    private Long accountNumber;

    private String name;

    @Column(length = 4,
            nullable = false)
    private String pin;

    @Column(columnDefinition = "numeric")
    private BigDecimal amount;

    public Account(String name, String pin) {
        this.name = name;
        this.pin = pin;
    }

    public Account(Long accountNumber, String name, String pin, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pin = pin;
        this.amount = amount;
    }

    public Account() {
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
