package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  int countByUsername(String username);

  @Override
  void delete(User user);

}