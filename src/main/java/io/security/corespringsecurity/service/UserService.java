package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.dto.UserDto;
import io.security.corespringsecurity.domain.entity.Account;

import java.util.List;

public interface UserService {
    void createUser(Account account);
    List<Account> getUsers();
    UserDto getUser(Long id);
    void deleteUser(Long idx);
}
