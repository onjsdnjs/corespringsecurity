package io.security.corespringsecurity.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthoritiesDto {

  private String roleName;
  private AntPathRequestMatcher antPathRequestMatcher;
}
