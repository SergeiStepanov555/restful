package com.example.restful.service;

import com.example.restful.domain.account.Account;
import com.example.restful.domain.transaction.Transaction;
import com.example.restful.repository.TransactionRepository;
import com.example.restful.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final Long ACCOUNT_NUMBER_FROM = 10L;
    private static final Long ACCOUNT_NUMBER_TO = 20L;
    private static final Long TRANSACTION_ID1 = 1L;
    private static final Long TRANSACTION_ID2 = 1L;
    private final static BigDecimal AMOUNT = BigDecimal.valueOf(1000);
    private final static LocalTime LOCAL_DATE_TIME = LocalTime.of(15, 30, 0, 0);
    private final static Transaction.Operation OPERATION_TRANSFER = Transaction.Operation.TRANSFER;

    private Transaction TRANSACTION_01 = Transaction
            .builder()
            .id(TRANSACTION_ID1)
            .accountNumberFrom(ACCOUNT_NUMBER_FROM)
            .accountNumberTo(ACCOUNT_NUMBER_TO)
            .amount(AMOUNT)
            .time(LOCAL_DATE_TIME)
            .operation(OPERATION_TRANSFER)
            .build();

    private Transaction TRANSACTION_02 = Transaction
            .builder()
            .id(TRANSACTION_ID2)
            .accountNumberFrom(ACCOUNT_NUMBER_FROM)
            .accountNumberTo(ACCOUNT_NUMBER_TO)
            .amount(AMOUNT)
            .time(LOCAL_DATE_TIME)
            .operation(OPERATION_TRANSFER)
            .build();

    private List<Transaction> mockTransactions = List.of(TRANSACTION_01, TRANSACTION_02);

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void saveTransactionTest() {
        transactionService.saveTransaction(ACCOUNT_NUMBER_FROM, ACCOUNT_NUMBER_TO, AMOUNT, OPERATION_TRANSFER);
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    public void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(mockTransactions);
        List<Transaction> result = transactionService.getAllTransactions();
        verify(transactionRepository, times(1)).findAll();
        assertEquals(mockTransactions, result);
    }

    @Test
    public void testGetAllTransactionsByAccountNumber() {
        when(transactionRepository.findTransactionsByAccountNumberFrom(ACCOUNT_NUMBER_FROM)).thenReturn(mockTransactions);
        List<Transaction> result = transactionService.getAllTransactionsByAccountNumber(ACCOUNT_NUMBER_FROM);
        verify(transactionRepository, times(1)).findTransactionsByAccountNumberFrom(ACCOUNT_NUMBER_FROM);
        assertEquals(mockTransactions, result);
    }
}




