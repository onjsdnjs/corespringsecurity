package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
  Account findByUsername(String username);
  int countByUsername(String username);
}