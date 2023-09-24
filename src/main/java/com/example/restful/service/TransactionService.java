package com.example.restful.service;


import com.example.restful.domain.transaction.Transaction;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionService {

    Transaction saveTransaction(Long accountNumberFrom, Long accountNumberTo, BigDecimal amount, Transaction.Operation operation);

    List<Transaction> getAllTransactions();

    List<Transaction> getAllTransactionsByAccountNumber(Long accountNumber);
}
