package com.example.restful.repository;

import com.example.restful.domain.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findTransactionsByAccountNumberFrom(Long accountNumber);
}
