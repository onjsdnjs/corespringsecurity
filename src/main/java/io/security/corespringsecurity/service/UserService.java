package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.dto.AccountDto;
import io.security.corespringsecurity.domain.entity.Account;

import java.util.List;

public interface UserService {

    List<Account> getUsers();
    AccountDto getUser(Long id);
    void createUser(Account account);
    void deleteUser(Long idx);
}
