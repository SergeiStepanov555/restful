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

}
