package io.security.corespringsecurity.security.authentication.services;

import io.security.corespringsecurity.domain.entity.Account;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class AccountContext extends User {
  private Account account;
  private List<String> roles;

  public AccountContext(Account account, List<String> roles) {
    super(account.getUsername(), account.getPassword(), roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    this.account = account;
    this.roles = roles;
  }
}
