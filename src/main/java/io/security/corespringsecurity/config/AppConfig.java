package io.security.corespringsecurity.config;

import io.security.corespringsecurity.repository.AccessIpRepository;
import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.security.configs.MethodSecurityConfig;
import io.security.corespringsecurity.service.RoleHierarchyService;
import io.security.corespringsecurity.service.SecurityResourceService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@AutoConfigureBefore({MethodSecurityConfig.class})
public class AppConfig {

    @Bean
    public SecurityResourceService securityResourceService(ResourcesRepository resourcesRepository, RoleHierarchyImpl roleHierarchy, RoleHierarchyService roleHierarchyService, AccessIpRepository accessIpRepository) {
        SecurityResourceService SecurityResourceService = new SecurityResourceService(resourcesRepository, roleHierarchy, roleHierarchyService, accessIpRepository);
        return SecurityResourceService;
    }
}
