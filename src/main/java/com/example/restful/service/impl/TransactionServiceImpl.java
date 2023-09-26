package com.example.restful.service.impl;

import com.example.restful.domain.transaction.Transaction;
import com.example.restful.repository.TransactionRepository;
import com.example.restful.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public Transaction saveTransaction(Long accountNumberFrom, Long accountNumberTo, BigDecimal amount, Transaction.Operation operation) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumberFrom(accountNumberFrom);
        transaction.setAccountNumberTo(accountNumberTo);
        transaction.setTime(LocalTime.now());
        transaction.setAmount(amount);
        transaction.setOperation(operation);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactionsByAccountNumber(Long accountNumber) {
        return transactionRepository.findTransactionsByAccountNumberFrom(accountNumber);
    }
}
