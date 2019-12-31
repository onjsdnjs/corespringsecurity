package io.security.corespringsecurity.config;

import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.security.configs.MethodSecurityConfig;
import io.security.corespringsecurity.service.SecurityResourceService;
import io.security.corespringsecurity.service.impl.RoleHierarchyServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@AutoConfigureBefore({MethodSecurityConfig.class})
public class AppConfig {

    @Bean
    public SecurityResourceService securityResourceService(ResourcesRepository resourcesRepository, RoleHierarchyImpl roleHierarchy, RoleHierarchyServiceImpl roleHierarchyService) {
        SecurityResourceService SecurityResourceService = new SecurityResourceService(resourcesRepository, roleHierarchy, roleHierarchyService);
        return SecurityResourceService;
    }
}
