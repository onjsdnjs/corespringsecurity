package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}