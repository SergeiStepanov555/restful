package com.example.restful.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.restful.domain.account.Account;

public interface AccountService {

	Account saveAccount(Account account);
	List<Account> getAllAccounts();
	void deposit(Long accountNumber, BigDecimal amount);
	void transfer(Long fromAccountNumber, Long toAccountNumber, BigDecimal amount, String pin);
	void withdraw(Long accountNumber, BigDecimal amount, String pin);


	

}
