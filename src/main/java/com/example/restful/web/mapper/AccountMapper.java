package com.example.restful.web.mapper;

import com.example.restful.domain.account.Account;
import com.example.restful.web.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "accountNumber", target = "accountNumber")
    AccountDto toDto(Account entity);

    @Mapping(source = "accountNumber", target = "accountNumber")
    List<AccountDto> toDto(List<Account> entity);

    Account toEntity(AccountDto dto);

    List<Account> toEntity(List<AccountDto> dtos);
}
