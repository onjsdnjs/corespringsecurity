package io.security.corespringsecurity.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleHierarchyDto {

  private String roldId;
  private String roleName;
  private String parentRoleId;
}
