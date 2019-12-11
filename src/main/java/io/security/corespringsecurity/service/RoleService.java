package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.entity.Role;

import java.util.List;

public interface RoleService {

    Role selectRole(long id);

    List<Role> selectRoles();

    void insertRole(Role role);
}