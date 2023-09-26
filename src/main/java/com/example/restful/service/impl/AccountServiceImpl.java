package com.example.restful.service.impl;

import com.example.restful.domain.account.Account;
import com.example.restful.domain.exception.ResourceNotFoundException;
import com.example.restful.domain.exception.WrongAmountException;
import com.example.restful.domain.exception.WrongPinException;
import com.example.restful.domain.exception.ZeroBalanceException;
import com.example.restful.domain.transaction.Transaction;
import com.example.restful.repository.AccountRepository;
import com.example.restful.service.AccountService;
import com.example.restful.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final TransactionService transactionService;

	@Override
	@Transactional
	public Account saveAccount(Account account) {
		account.setAccountNumber(Math.abs(new Random().nextLong()));
		account.setAmount(BigDecimal.valueOf(0));
		return accountRepository.save(account);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Account> getAllAccounts() {
		List<Account> accounts = new ArrayList<>();
		accountRepository.findAll().forEach(accounts::add);
		return accounts;
	}


	@Override
	@Transactional
	public void deposit(Long accountNumber, BigDecimal amount) {
		Account account = accountRepository.findById(accountNumber)
				.orElseThrow(() ->
						new ResourceNotFoundException("Account not found."));
		account.setAmount(account.getAmount().add(amount));
		accountRepository.save(account);
		transactionService.saveTransaction(null, accountNumber, amount, Transaction.Operation.DEPOSIT);
	}


	@Override
	@Transactional
	public void transfer(Long fromAccountNumber, Long toAccountNumber, BigDecimal amount, String pin) {
		Account fromAccount = accountRepository.findById(fromAccountNumber)
				.orElseThrow(() ->
						new ResourceNotFoundException("Account not found."));
		if (!fromAccount.getPin().equals(pin)) {
			throw new WrongPinException("Wrong pin");
		}
		Account toAccount = accountRepository.findById(toAccountNumber)
				.orElseThrow(() ->
						new ResourceNotFoundException("Account not found."));

		if (fromAccount.getAmount().longValue() < amount.longValue()) {
			throw new WrongAmountException("Wrong amount");
		}
		fromAccount.setAmount(fromAccount.getAmount().subtract(amount));
		toAccount.setAmount(toAccount.getAmount().add(amount));
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		transactionService.saveTransaction(fromAccountNumber, toAccountNumber, amount, Transaction.Operation.TRANSFER);
	}


	@Override
	@Transactional
	public void withdraw(Long accountNumber, BigDecimal amount, String pin) {
		Account account = accountRepository.findById(accountNumber)
				.orElseThrow(() ->
						new ResourceNotFoundException("Account not found."));

		if (!pin.equals(account.getPin())) {
			throw new WrongPinException("Wrong pin");
		}
		BigDecimal currentBalance = account.getAmount();
		if (currentBalance.equals(BigDecimal.ZERO)) {
			throw new ZeroBalanceException("Zero balance");
		}
		if (currentBalance.compareTo(amount) < 0) {
			throw new WrongAmountException("Wrong amount");
		}
		account.setAmount(account.getAmount().subtract(amount));
		accountRepository.save(account);
		transactionService.saveTransaction(accountNumber, accountNumber, amount, Transaction.Operation.WITHDRAW);
	}
}
