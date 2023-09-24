package com.example.restful.domain.transaction;



import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

@Builder
@Data
@Entity
@Table
public class Transaction {

    public enum Operation {

        DEPOSIT,
        TRANSFER,
        WITHDRAW
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long accountNumberFrom;

    private Long accountNumberTo;

    private LocalTime time;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    public Transaction() {
    }

    public Transaction(Long accountNumberFrom, Long accountNumberTo, LocalTime time, BigDecimal amount, Operation operation) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.time = time;
        this.amount = amount;
        this.operation = operation;
    }

    public Transaction(Long id, Long accountNumberFrom, Long accountNumberTo, LocalTime time, BigDecimal amount, Operation operation) {
        this.id = id;
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.time = time;
        this.amount = amount;
        this.operation = operation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountNumberFrom() {
        return accountNumberFrom;
    }

    public void setAccountNumberFrom(Long accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }

    public Long getAccountNumberTo() {
        return accountNumberTo;
    }

    public void setAccountNumberTo(Long accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(accountNumberFrom, that.accountNumberFrom))
            return false;
        if (!Objects.equals(accountNumberTo, that.accountNumberTo))
            return false;
        if (!Objects.equals(time, that.time)) return false;
        if (!Objects.equals(amount, that.amount)) return false;
        return operation == that.operation;
    }
}
