package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.entity.Account;

public interface UserService {

    void createUser(Account account);
}
