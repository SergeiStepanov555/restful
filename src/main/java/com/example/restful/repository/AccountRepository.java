package com.example.restful.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.restful.domain.account.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
