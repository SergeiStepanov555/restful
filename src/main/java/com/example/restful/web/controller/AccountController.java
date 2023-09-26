package com.example.restful.web.controller;

import com.example.restful.domain.account.Account;
import com.example.restful.domain.success.Success;
import com.example.restful.service.AccountService;
import com.example.restful.web.dto.AccountDto;
import com.example.restful.web.dto.validation.OnCreate;
import com.example.restful.web.dto.validation.OnDeduct;
import com.example.restful.web.dto.validation.OnDeposit;
import com.example.restful.web.mapper.AccountMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "Account API")
@RequestMapping("/api/v1/accounts")
public class AccountController {

	private final AccountService accountService;
	private final AccountMapper accountMapper;

	@Operation(summary = "Creating a new Account")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public AccountDto saveAccount(@Validated(OnCreate.class) @RequestBody AccountDto accountDto) {
		Account account = accountMapper.toEntity(accountDto);
		accountService.saveAccount(account);
		return accountMapper.toDto(account);
	}

	@Operation(summary = "Get all Accounts")
	@GetMapping()
	public List<AccountDto> getAllAccounts(){
		List<Account> accounts = accountService.getAllAccounts();
	 	return accountMapper.toDto(accounts);
	}

	@Operation(summary = "Deposit to Account")
	@PatchMapping(value = "/{accountNumber}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deposit(@PathVariable Long accountNumber, @RequestBody @Validated(OnDeposit.class) AccountDto accountDto) {
		BigDecimal amount = accountDto.getAmount();
		accountService.deposit(accountNumber, amount);
		return new ResponseEntity<>(Success.SUCCESS_DEPOSIT.getValueOfSuccess(), HttpStatus.OK);
	}

	@Operation(summary = "Transfer between Accounts")
	@PatchMapping(value = "/{fromAccountNumber}/transfer/{toAccountNumber}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> transfer(@PathVariable Long fromAccountNumber,
										   @PathVariable Long toAccountNumber,
										   @Validated(OnDeduct.class) @RequestBody AccountDto accountDto) {
		BigDecimal amount = accountDto.getAmount();
		String pin = accountDto.getPin();
		accountService.transfer(fromAccountNumber, toAccountNumber, amount, pin);
		return new ResponseEntity<>(Success.SUCCESS_TRANSFER.getValueOfSuccess(), HttpStatus.OK);
	}

	@Operation(summary = "Withdraw from Account")
	@PatchMapping(value = "/{accountNumber}/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> withdraw(@PathVariable Long accountNumber,
										   @Validated @RequestBody AccountDto accountDto) {
		BigDecimal amount = accountDto.getAmount();
		String pin = accountDto.getPin();
		accountService.withdraw(accountNumber, amount, pin);
		return new ResponseEntity<>(Success.SUCCESS_WITHDRAW.getValueOfSuccess(), HttpStatus.OK);
	}
}
