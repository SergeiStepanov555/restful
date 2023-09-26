package com.example.restful.web.controller;

import com.example.restful.domain.transaction.Transaction;
import com.example.restful.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Transaction Controller", description = "Transaction API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Get all Transactions")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(allTransactions, HttpStatus.OK);
    }

    @Operation(summary = "Get all Transactions by Account Number")
    @GetMapping(value = "/{accountNumber}")
    public ResponseEntity<List<Transaction>> getAllTransactionsById(@PathVariable Long accountNumber) {
        List<Transaction> allTransactionsByAccountNumber = transactionService.getAllTransactionsByAccountNumber(accountNumber);
        return new ResponseEntity<>(allTransactionsByAccountNumber, HttpStatus.OK);
    }
}
