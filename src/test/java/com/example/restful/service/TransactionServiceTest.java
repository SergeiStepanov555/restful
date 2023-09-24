package com.example.restful.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final Long ACCOUNT_NUMBER_FROM = 10L;
    private static final Long ACCOUNT_NUMBER_TO = 20L;
    private static final Long TRANSACTION_ID = 1L;
    private final static BigDecimal AMOUNT = BigDecimal.valueOf(1000);
    private final static LocalTime LOCAL_DATE_TIME = LocalTime.of(12, 0);
    private final static Transaction.Operation OPERATION_TRANSFER = Transaction.Operation.TRANSFER;

    private Transaction TRANSACTION = Transaction
            .builder()
            .id(TRANSACTION_ID)
            .accountNumberFrom(ACCOUNT_NUMBER_FROM)
            .accountNumberTo(ACCOUNT_NUMBER_TO)
            .amount(AMOUNT)
            .time(LOCAL_DATE_TIME)
            .operation(OPERATION_TRANSFER)
            .build();

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void saveTransactionTest() {
        // Создаем ожидаемый объект Transaction
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAccountNumberFrom(ACCOUNT_NUMBER_FROM);
        expectedTransaction.setAccountNumberTo(ACCOUNT_NUMBER_TO);
        expectedTransaction.setTime(LocalTime.now());
        expectedTransaction.setAmount(AMOUNT);
        expectedTransaction.setOperation(OPERATION_TRANSFER);

        // Настраиваем mock для transactionRepository
        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);

        // Вызываем метод, который хотим протестировать
        Transaction savedTransaction = transactionService.saveTransaction(ACCOUNT_NUMBER_FROM, ACCOUNT_NUMBER_TO, AMOUNT, OPERATION_TRANSFER);

        // Проверяем, что метод save был вызван с ожидаемым объектом Transaction
        verify(transactionRepository, times(1)).save(expectedTransaction);

        // Проверяем, что возвращенный объект Transaction соответствует ожидаемому
        assertEquals(expectedTransaction, savedTransaction);
    }
}


