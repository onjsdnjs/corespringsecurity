package io.security.corespringsecurity.service.impl;

import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.repository.UserRepository;
import io.security.corespringsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(Account account){
        userRepository.save(account);
    }
}