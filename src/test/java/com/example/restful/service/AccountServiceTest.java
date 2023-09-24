package com.example.restful.service;

import com.example.restful.domain.account.Account;
import com.example.restful.domain.exception.ResourceNotFoundException;
import com.example.restful.domain.exception.WrongPinException;
import com.example.restful.domain.exception.ZeroBalanceException;
import com.example.restful.domain.transaction.Transaction;
import com.example.restful.repository.AccountRepository;
import com.example.restful.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    private static final Long ACCOUNT_NUMBER_ADAM = 731L;
    private static final String NAME_ADAM = "Adam";
    private static final String PIN_ADAM = "1234";

    private static final Long ACCOUNT_NUMBER_EVE = 626L;
    private static final String NAME_EVE = "Eve";
    private static final String PIN_EVE = "5555";

    private static final BigDecimal AMOUNT = BigDecimal.ZERO;
    private static final BigDecimal AMOUNT_TO_DEPOSIT = BigDecimal.valueOf(1000.00);
    private static final BigDecimal AMOUNT_TO_TRANSFER = BigDecimal.valueOf(1000.00);
    private static final BigDecimal AMOUNT_TO_WITHDRAW = BigDecimal.valueOf(1000.00);
    private static final Long WRONG_ACCOUNT_NUMBER = 1L;
    private static final String WRONG_PIN = "1010";

    private Account ADAM = Account
            .builder()
            .accountNumber(ACCOUNT_NUMBER_ADAM)
            .name(NAME_ADAM)
            .pin(PIN_ADAM)
            .amount(AMOUNT)
            .build();

    private Account EVE = Account
            .builder()
            .accountNumber(ACCOUNT_NUMBER_EVE)
            .name(NAME_EVE)
            .pin(PIN_EVE)
            .amount(AMOUNT)
            .build();

    private List<Account> accounts = List.of(ADAM, EVE);

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountServiceImpl accountService;


    @Test
    void saveAccountTest() {
        accountService.saveAccount(ADAM);
        accountService.saveAccount(EVE);
        verify(accountRepository, times(1)).save(ADAM);
        verify(accountRepository, times(1)).save(EVE);
    }

    @Test
    void getAllAccountTest() {
        doReturn(accounts).when(accountRepository).findAll();
        var expected = accounts;
        var actual = accountService.getAllAccounts();
        verify(accountRepository, times(1)).findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void depositTestIfAccountExist() {
        when(accountRepository.findById(ACCOUNT_NUMBER_ADAM)).thenReturn(Optional.of(ADAM));
        accountService.deposit(ACCOUNT_NUMBER_ADAM, AMOUNT_TO_DEPOSIT);
        assertThat(ADAM.getAmount()).isEqualTo(AMOUNT_TO_DEPOSIT);
        verify(accountRepository, times(1)).save(ADAM);
        verify(transactionService, times(1)).saveTransaction(
                null, ACCOUNT_NUMBER_ADAM, AMOUNT_TO_DEPOSIT, Transaction.Operation.DEPOSIT
        );
    }

    @Test
    void depositThrowsExceptionWhenAccountNotFound() {
        when(accountRepository.findById(WRONG_ACCOUNT_NUMBER)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deposit(WRONG_ACCOUNT_NUMBER, AMOUNT_TO_DEPOSIT);
        });
        verify(accountRepository, never()).save(any());
        verify(transactionService, never()).saveTransaction(any(), any(), any(), any());
    }

    @Test
    void transferSuccessful() {
        ADAM.setAmount(AMOUNT_TO_DEPOSIT);
        when(accountRepository.findById(ACCOUNT_NUMBER_ADAM)).thenReturn(Optional.of(ADAM));
        when(accountRepository.findById(ACCOUNT_NUMBER_EVE)).thenReturn(Optional.of(EVE));
        accountService.transfer(ACCOUNT_NUMBER_ADAM, ACCOUNT_NUMBER_EVE, AMOUNT_TO_TRANSFER, PIN_ADAM);
        assertEquals(new BigDecimal("0.0"), ADAM.getAmount());
        assertEquals(new BigDecimal("1000.0"), EVE.getAmount());
        verify(accountRepository, times(1)).save(ADAM);
        verify(accountRepository, times(1)).save(EVE);
        verify(transactionService, times(1)).saveTransaction(ACCOUNT_NUMBER_ADAM, ACCOUNT_NUMBER_EVE, AMOUNT_TO_TRANSFER, Transaction.Operation.TRANSFER);
    }

    @Test
    void transferThrowsResourceNotFoundExceptionWhenFromAccountNotFound() {
        doReturn(Optional.of(ADAM)).when(accountRepository).findById(ACCOUNT_NUMBER_ADAM);
        doReturn(Optional.empty()).when(accountRepository).findById(WRONG_ACCOUNT_NUMBER);
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.transfer(ACCOUNT_NUMBER_ADAM, WRONG_ACCOUNT_NUMBER, AMOUNT, PIN_ADAM);
        });
        verify(accountRepository, never()).save(any());
        verify(transactionService, never()).saveTransaction(any(), any(), any(), any());
    }

    @Test
    void transferThrowsWrongPinExceptionExceptionWhenFromAccountNotFound() {
        ADAM.setAmount(AMOUNT_TO_DEPOSIT);
        lenient().doReturn(Optional.of(ADAM)).when(accountRepository).findById(ACCOUNT_NUMBER_ADAM);
        lenient().doReturn(Optional.of(EVE)).when(accountRepository).findById(ACCOUNT_NUMBER_EVE);
        assertThrows(WrongPinException.class, () -> {
            accountService.transfer(ACCOUNT_NUMBER_ADAM, WRONG_ACCOUNT_NUMBER, AMOUNT, WRONG_PIN);
        });
        verify(accountRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
        verify(transactionService, never()).saveTransaction(any(), any(), any(), any());
    }

    @Test
    void withdrawSuccessful() {
        ADAM.setAmount(AMOUNT_TO_DEPOSIT);
        lenient().doReturn(Optional.of(ADAM)).when(accountRepository).findById(ACCOUNT_NUMBER_ADAM);
        accountService.withdraw(ACCOUNT_NUMBER_ADAM, AMOUNT_TO_WITHDRAW, PIN_ADAM);
        assertEquals(new BigDecimal("0.0"), ADAM.getAmount());
        verify(accountRepository, times(1)).save(ADAM);
        verify(transactionService, times(1)).saveTransaction(ACCOUNT_NUMBER_ADAM, ACCOUNT_NUMBER_ADAM, AMOUNT_TO_DEPOSIT, Transaction.Operation.WITHDRAW);
    }

    @Test
    void withdrawThrowsZeroBalanceExceptionWhenBalanceIsZero() {
        lenient().doReturn(Optional.of(ADAM)).when(accountRepository).findById(ACCOUNT_NUMBER_ADAM);
        assertThrows(ZeroBalanceException.class, () -> {
            accountService.withdraw(ACCOUNT_NUMBER_ADAM, AMOUNT_TO_WITHDRAW, PIN_ADAM);
            verify(accountRepository, never()).save(any());
            verify(transactionService, never()).saveTransaction(any(), any(), any(), any());
        });
    }
}


