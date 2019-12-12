package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.dto.UserDto;
import io.security.corespringsecurity.domain.entity.User;

import java.util.List;

public interface UserService {

  List<User> getUsers();
  UserDto getUser(Long id);
  void createUser(User user);
  void deleteUser(Long idx);
}
