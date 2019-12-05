package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  int countByUsername(String username);

  User findByEmail(String email);

  @Override
  void delete(User user);

  Optional<User> findByEmailIgnoreCase(String email);
}